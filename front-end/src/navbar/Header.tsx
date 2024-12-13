import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';



const Header: React.FC = () => {
  const { isAuthenticated, logout } = useAuth();

  const logoutClick = () => {
    logout();
  }
  return (
    <>
    <header className="bg-blue-600 text-white p-4 shadow-md">
      <div className="container mx-auto flex justify-between items-center">
        <h1 className="text-2xl font-bold">
          <Link to={!isAuthenticated ? "/login" : "/home"}>Expense Management</Link>
        </h1>
        <nav className="space-x-4">
          {isAuthenticated ? (
            <>
              <Link to="/home" className="hover:underline">Dashboard</Link>
              <Link to="/profile" className="hover:underline">Profile</Link>
              <Link to="/expenses" className="hover:underline">Expenses</Link>
              <button onClick={logoutClick} className="hover:underline">Logout</button>
            </>
          ) : (
              <>
              <Link to={`/login`} className="hover:underline">Login</Link>
              <Link to="/register" className="hover:underline">Register</Link>
              </>
          )}
        </nav>
      </div>
    </header>
    </>
  );
};

export default Header;
