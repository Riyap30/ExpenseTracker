import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

const Header: React.FC = () => {
  const { isAuthenticated, logout } = useAuth();

  const logoutClick = () => {
    logout();
  };

  return (
    <header className="flex shadow-md">
      {/* Left Side - Teal Background */}
      <div className="flex items-center justify-center w-1/2 bg-teal-500 p-4">
        <h1 className="text-3xl font-extrabold text-white">
          <Link to={!isAuthenticated ? "/login" : "/home"} className="hover:text-teal-200 transition-colors">
            SplitwiseAI
          </Link>
        </h1>
      </div>

      {/* Right Side - Dark Gray Background */}
      <div className="flex items-center justify-end w-1/2 bg-gray-900 p-4">
        <nav className="space-x-6 text-white text-lg font-medium">
          {isAuthenticated ? (
            <>
              <Link to="/home" className="hover:text-teal-300 transition-colors">
                Dashboard
              </Link>
              <Link to="/profile" className="hover:text-teal-300 transition-colors">
                Profile
              </Link>
              <Link to="/expenses" className="hover:text-teal-300 transition-colors">
                Expenses
              </Link>
              <button onClick={logoutClick} className="hover:text-teal-300 transition-colors">
                Logout
              </button>
            </>
          ) : (
            <>
              <Link to="/login" className="hover:text-teal-300 transition-colors">
                Login
              </Link>
              <Link to="/register" className="hover:text-teal-300 transition-colors">
                Register
              </Link>
            </>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
