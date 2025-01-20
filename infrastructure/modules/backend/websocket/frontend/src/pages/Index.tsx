import Navbar from "@/components/Navbar";
import Hero from "@/components/Hero";
import AiAssistantButton from "@/components/AiAssistantButton";

const Index = () => {
  return (
    <div className="min-h-screen bg-gray-50">
      <Navbar />
      <Hero />
      <AiAssistantButton />
    </div>
  );
};

export default Index;