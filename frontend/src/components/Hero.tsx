const Hero = () => {
  return (
    <div className="bg-gradient-to-r from-medical-secondary to-medical-primary py-20">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="text-center">
          <h1 className="text-4xl md:text-5xl font-bold text-white mb-6">
            Your Health, Our Priority
          </h1>
          <p className="text-xl text-white/90 mb-8 max-w-2xl mx-auto">
            Experience world-class healthcare with our team of expert doctors and state-of-the-art facilities.
          </p>
          <button className="bg-white text-medical-primary px-8 py-3 rounded-full font-semibold hover:bg-gray-100 transition-colors">
            Book Appointment
          </button>
        </div>
      </div>
    </div>
  );
};

export default Hero;