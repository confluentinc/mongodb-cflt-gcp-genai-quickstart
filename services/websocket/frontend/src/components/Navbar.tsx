import { useState } from "react";
import { Menu, X } from "lucide-react";
import Logo from "./Logo";

const Navbar = () => {
  const [isOpen, setIsOpen] = useState(false);

  return (
    <nav className="bg-white shadow-md">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between h-16">
          <div className="flex items-center">
            <Logo />
          </div>
          
          {/* Desktop Menu */}
          <div className="hidden md:flex items-center space-x-8">
            <a href="#" className="text-gray-700 hover:text-medical-primary">Home</a>
            <a href="#" className="text-gray-700 hover:text-medical-primary">Services</a>
            <a href="#" className="text-gray-700 hover:text-medical-primary">Doctors</a>
            <a href="#" className="text-gray-700 hover:text-medical-primary">About</a>
            <a href="#" className="text-gray-700 hover:text-medical-primary">Contact</a>
          </div>

          {/* Mobile menu button */}
          <div className="md:hidden flex items-center">
            <button
              onClick={() => setIsOpen(!isOpen)}
              className="text-gray-700 hover:text-medical-primary"
            >
              {isOpen ? <X size={24} /> : <Menu size={24} />}
            </button>
          </div>
        </div>

        {/* Mobile Menu */}
        {isOpen && (
          <div className="md:hidden">
            <div className="px-2 pt-2 pb-3 space-y-1 sm:px-3">
              <a href="#" className="block px-3 py-2 text-gray-700 hover:text-medical-primary">Home</a>
              <a href="#" className="block px-3 py-2 text-gray-700 hover:text-medical-primary">Services</a>
              <a href="#" className="block px-3 py-2 text-gray-700 hover:text-medical-primary">Doctors</a>
              <a href="#" className="block px-3 py-2 text-gray-700 hover:text-medical-primary">About</a>
              <a href="#" className="block px-3 py-2 text-gray-700 hover:text-medical-primary">Contact</a>
            </div>
          </div>
        )}
      </div>
    </nav>
  );
};

export default Navbar;