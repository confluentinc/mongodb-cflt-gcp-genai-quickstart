import { Heart } from "lucide-react";

const Logo = () => {
  return (
    <div className="flex items-center space-x-2">
      <div className="flex items-center">
        <Heart className="w-8 h-8 text-medical-primary" fill="#2563EB" />
      </div>
      <div className="flex flex-col items-start">
        <span className="text-xl font-bold text-medical-primary leading-tight">MedGuide</span>
        <span className="text-sm text-medical-secondary leading-tight">Health</span>
      </div>
    </div>
  );
};

export default Logo;