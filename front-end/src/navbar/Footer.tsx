import React from 'react';



const Footer: React.FC = () => {
  return (
    <>
    <footer className="bg-gray-800 text-white p-4 mt-auto">
      <div className="container mx-auto text-center">
        <p className="text-sm">&copy; {new Date().getFullYear()} MyApp. All rights reserved.</p>
        <p className="text-sm mt-2">
          <a href="/terms" className="hover:underline">Terms of Service</a> | 
          <a href="/privacy" className="hover:underline ml-2">Privacy Policy</a>
        </p>
      </div>
    </footer>
    </>
  );
};

export default Footer;
