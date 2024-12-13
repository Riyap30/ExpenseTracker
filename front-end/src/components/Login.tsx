import React, { useEffect, useState } from 'react';
import '../index.css';

export default function Login() {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState<string| null>(null);
    const update = localStorage.getItem("update");
    const [passwordError, setPasswordError] = useState<string | null>(null);
    const isUpdate = localStorage.getItem("isUpdate");
    useEffect(() => {
        
        localStorage.setItem("update", "");
        localStorage.setItem("isUpdate", false.toString());
    }, []);

    // Validate password
    const validatePassword = (password: string): string | null => {
      if (password.length < 8) {
        return 'Password must be at least 8 characters long.';
      }
      if (!/[A-Z]/.test(password)) {
        return 'Password must contain at least one uppercase letter.';
      }
      if (!/[a-z]/.test(password)) {
        return 'Password must contain at least one lowercase letter.';
      }
      if (!/[0-9]/.test(password)) {
        return 'Password must contain at least one number.';
      }
      if (!/[!@#$%^&*]/.test(password)) {
        return 'Password must contain at least one special character (!@#$%^&*).';
      }
      return null;
    };
    
    // Handle form input changes
    const handleUsernameChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setUsername(e.target.value);
    };

    // Handle form input changes
    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newPassword = e.target.value;
        setPassword(newPassword);

        const error = validatePassword(newPassword);
        setPasswordError(error);
    };

    // Handle form submission
    const handleSubmit = async (e: React.FormEvent<HTMLFormElement>) => {
        e.preventDefault();
        setError(null);

        try {
            const response = await fetch(`http://localhost:8081/auth/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                },
                body: JSON.stringify({ username, password }),
            });

            if (!response.ok) {
                const errorMessage = await response.json().then((data) => data.absolute);
                throw new Error(errorMessage);
            }

            const data = await response.json();
            const token = data.token;

            localStorage.setItem("token", token);

            window.location.href = "/home";
        } catch (error) {
            setError("Login Failed " + error);
        }
    };   

    return (
        <>
    <div className="flex items-center justify-center min-h-screen bg-gray-100">
        <div className="w-full max-w-md p-8 space-y-4 bg-white rounded shadow-lg">
          <h2 className="text-2xl font-bold text-center">Login</h2>
          {isUpdate === "true" && <p className="text-red-500">{update} has been updated</p>}
          <form onSubmit={handleSubmit} className="space-y-4">
            <div>
              <label htmlFor="email" className="block text-sm font-medium text-gray-700">
                Username
              </label>
              <input
                type="name"
                id="email"
                value={username}
                onChange={handleUsernameChange}
                className="w-full p-2 mt-1 border border-gray-300 rounded"
                required
              />
            </div>
            <div>
              <label htmlFor="password" className="block text-sm font-medium text-gray-700">
                Password
              </label>
              <input
                type="password"
                id="password"
                value={password}
                onChange={handlePasswordChange}
                className="w-full p-2 mt-1 border border-gray-300 rounded"
                required
              />
              {passwordError && <p className="text-red-500">{passwordError}</p>}
            </div>
            <div className="flex items-center justify-between">
              <a href="/forgot-password" className="text-sm text-blue-600 hover:underline">
                Forgot Password?
              </a>
              {error && <p className="text-red-500">{error}</p>}
              
              <button
                type="submit"
                className="px-4 py-2 text-white bg-blue-600 rounded hover:bg-blue-700"
              >
                Login
              </button>

            </div>
          </form>
        </div>
      </div>
      </>
    );
}
