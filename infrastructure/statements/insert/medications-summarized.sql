insert into
    `medications_summarized`
select
    `id`,
    `name`,
    `generic_name`,
    `brand_names`,
    `dosage_form`,
    `strength`,
    `route_of_administration`,
    `frequency_per_day`,
    `duration_in_days`,
    `side_effects`,
    `disclaimers`,
    `illnesses_treated`,
    `drug_class`,
    `prescription_required`,
    `pregnancy_category`,
    `alcohol_interaction`,
    `contraindications`,
    `storage_instructions`,
    `manufacturers`,
    `cost_per_unit`,
    `country_availability`,
    `expiry_date`,
     response
from
    `gcs_medications`,
    LATERAL TABLE (
        ML_PREDICT (
            'GCPGeneralModel',
            (
'
<instructions>
You are a summarization assistant tasked with generating concise, accurate summaries of medication data for indexing in a vector database. Your summary must include all key information such as the required side effects, duration, frequency, name, and any other significant details provided in the input. The output should be a single-paragraph summary suitable for indexing purposes. **Do not include any tags (e.g., XML or JSON) in the response. Only provide plain text.**
</instructions>

<context>
This task involves summarizing structured medications objects into natural language. Make sure all important fields are represented accurately, with special attention to differentiating between medications (e.g., side effects) and other details. The goal is to capture the essence of the medication data so it can be easily retrieved based on similarity.
</context>

<examples>
<example>
<input>
  {
    "id": "67b96882",
    "name": "Ibuprofen",
    "generic_name": "IBUPROFEN",
    "brand_names": [
      "Ibuprofen Dye Free"
    ],
    "dosage_form": "Directions do not take more than directed the smallest effective dose should be used adults and children 12 years and over: take 1 tablet every 4 to 6 hours while symptoms persist if pain or fever does not respond to 1 tablet, 2 tablets may be used do not exceed 6 tablets in 24 hours, unless directed by a doctor children under 12 years: ask a doctor",
    "strength": "500mg",
    "route_of_administration": "ORAL",
    "frequency_per_day": 2,
    "duration_in_days": 30,
    "side_effects": [
      "Nausea",
      "Heartburn",
      "Dizziness",
      "Stomach pain"
    ],
    "disclaimers": [
      "Warnings Allergy alert: Ibuprofen may cause a severe allergic reaction, especially in people allergic to aspirin..."
    ],
    "illnesses_treated": [
      "Uses temporarily relieves minor aches and pains due to: headache toothache backache menstrual cramps the common cold muscular aches minor pain of arthritis temporarily reduces fever"
    ],
    "drug_class": "NSAID",
    "prescription_required": true,
    "pregnancy_category": "C",
    "alcohol_interaction": "Moderate",
    "contraindications": [
      "None listed"
    ],
    "storage_instructions": "Store between 20°C-25°C (68°F-77°F)",
    "manufacturers": [
      "CVS Pharmacy"
    ],
    "cost_per_unit": 4.93,
    "country_availability": [
      "Canada",
      "USA",
      "France",
      "Germany",
      "India"
    ],
    "expiry_date": "2029-08-05"
  }
</input>
<output>
Ibuprofen (500mg) is a non-steroidal anti-inflammatory drug (NSAID) used to relieve minor aches and pains from headaches, toothaches, backaches, menstrual cramps, colds, muscular aches, and minor arthritis, as well as to reduce fever. It is taken orally with a recommended dosage of 1 tablet every 4-6 hours, with a maximum of 6 tablets per day unless directed by a doctor. Common side effects include nausea, heartburn, dizziness, and stomach pain, and it carries an allergy warning, particularly for individuals sensitive to aspirin. Moderate alcohol interaction may increase the risk of stomach irritation. It is classified as Pregnancy Category C, meaning caution is advised for pregnant individuals. A prescription is required, and it is manufactured by CVS Pharmacy, available in Canada, USA, France, Germany, and India. It should be stored between 20°C-25°C (68°F-77°F) and has an expiration date of August 5, 2029.
</output>
</example>
</examples>

<format>
Respond in a concise single-paragraph summary format, including all critical medications details. Avoid introducing extra commentary or unrelated information.
</format>

<persona>
You are a precise and detail-oriented assistant trained to create summaries for structured data. Ensure your tone is professional and factual.
</persona>

<task>
Please process the following medication object and provide a summary:
<medication_object>
{
    "name": "' || `name` || '",
    "generic_name": "' || generic_name || '",
    "brand_names": "[' || ARRAY_JOIN(brand_names, ', ') || ']",
    "dosage_form": "' || dosage_form || '",
    "strength": "' || strength || '",
    "route_of_administration": "' || route_of_administration || '",
    "frequency_per_day": "' || CAST(frequency_per_day AS STRING)  || '",
    "duration_in_days": "' || CAST(duration_in_days AS STRING) || '",
    "side_effects": "[' || ARRAY_JOIN(side_effects, ', ') || ']",
    "disclaimers": "[' || ARRAY_JOIN(disclaimers, ', ') || ']",
    "illnesses_treated": "[' || ARRAY_JOIN(illnesses_treated, ', ') || ']",
    "drug_class": "' || drug_class || '",
    "prescription_required": "' || CAST(prescription_required AS STRING) || '",
    "pregnancy_category": "' || pregnancy_category || '",
    "alcohol_interaction": "' || alcohol_interaction || '",
    "contraindications": "[' || ARRAY_JOIN(contraindications, ', ') || ']",
    "storage_instructions": "' || storage_instructions || '",
    "manufacturers": "[' || ARRAY_JOIN(manufacturers, ', ') || ']",
    "cost_per_unit": "' || CAST(cost_per_unit AS STRING) || '",
    "country_availability": "[' || ARRAY_JOIN(country_availability, ', ') || ']",
    "expiry_date": "' || expiry_date || '"
}
</medication_object>
</task>
'
            )
        )
    );