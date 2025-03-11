import os
import json
from pymongo import MongoClient
import certifi
from sentence_transformers import SentenceTransformer


# Fetch environment variables (assuming they are always set)
MONGODB_USER = os.getenv("MONGODB_USER")
MONGODB_PASSWORD = os.getenv("MONGODB_PWD")
MONGODB_CLUSTER = os.getenv("MONGODB_CLUSTER")


# Ensure none of the required variables are None
if not all([MONGODB_USER, MONGODB_PASSWORD, MONGODB_CLUSTER]):
    raise ValueError("Missing one or more required MongoDB environment variables.")

# Construct MongoDB URI safely
MONGO_URI = f"mongodb+srv://{MONGODB_USER}:{MONGODB_PASSWORD}@{MONGODB_CLUSTER}/?retryWrites=true&w=majority&tlsCAFile=" + certifi.where()


DATABASE_NAME = os.getenv("MONGODB_DATABASE")
COLLECTION_NAME = os.getenv("MONGODB_COLLECTION")

# Paths
BASE_DIR = os.path.dirname(os.path.abspath(__file__))  # Get script directory
json_file_path = os.path.join(BASE_DIR, "infrastructure/data/openfda_prescription_drugs.json")

# Load SentenceTransformer model for embedding generation
embedding_model = SentenceTransformer("all-MiniLM-L6-v2")

def connect_to_mongodb():
    """Connect to MongoDB and return the collection."""
    try:
        client = MongoClient(MONGO_URI)
        db = client[DATABASE_NAME]
        collection = db[COLLECTION_NAME]
        print(f"Connected to MongoDB database: {DATABASE_NAME}, collection: {COLLECTION_NAME}")
        return collection
    except Exception as e:
        print(f"Error connecting to MongoDB: {e}")
        exit(1)

def load_json_data(filepath):
    """Load JSON data from a file."""
    try:
        with open(filepath, "r", encoding="utf-8") as f:
            data = json.load(f)
        print(f"Loaded {len(data)} records from JSON file.")
        return data
    except Exception as e:
        print(f"Error loading JSON data: {e}")
        exit(1)

def generate_embeddings(data):
    """Generate embeddings for each drug record and update the record."""
    for record in data:
        text_to_embed = f"{record['name']} {record['generic_name']} {' '.join(record.get('illnesses_treated', []))}"
        record["embedding"] = embedding_model.encode(text_to_embed).tolist()
    print("Generated embeddings for all records.")
    return data

def insert_into_mongodb(collection, data):
    """Insert the processed data into MongoDB."""
    try:
        collection.insert_many(data)
        print(f"Inserted {len(data)} records into MongoDB.")
    except Exception as e:
        print(f"Error inserting data into MongoDB: {e}")
        exit(1)


def create_vector_index():
    client = MongoClient(MONGO_URI)
    db = client[DATABASE_NAME]

    # Create search index definition
    search_index = {
        "mappings": {
            "dynamic": True,
            "fields": {
                "embedding": {
                    "type": "knnVector",
                    "dimensions": 384,  # Match the embedding model output size
                    "similarity": "cosine"
                }
            }
        }
    }

    # Create the index
    db.command({
        "createSearchIndexes": COLLECTION_NAME,
        "indexes": [{"name": "vector_index", "definition": search_index}]
    })

    print("Vector search index created successfully!")

if __name__ == "__main__":
    collection = connect_to_mongodb()
    drug_data = load_json_data(json_file_path)
    enriched_data = generate_embeddings(drug_data)
    insert_into_mongodb(collection, enriched_data)
    create_vector_index()
