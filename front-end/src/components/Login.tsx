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
    <div className="flex items-center justify-center min-h-screen bg-gray-50">
      <div className="w-full max-w-md p-6 bg-white rounded-lg shadow-md">
        {/* Header */}
        <h2 className="text-3xl font-extrabold text-center text-gray-800">Welcome Back</h2>
        <p className="mt-1 text-sm text-center text-gray-500">
          Please sign in to your account
        </p>

        {/* Update Notification */}
        {isUpdate === "true" && (
          <div className="mt-4 p-3 text-sm text-red-700 bg-red-100 rounded">
            {update} has been updated
          </div>
        )}

        {/* Login Form */}
        <form onSubmit={handleSubmit} className="mt-6 space-y-5">
          {/* Username Field */}
          <div>
            <label htmlFor="username" className="block text-sm font-medium text-gray-600">
              Username
            </label>
            <input
              type="text"
              id="username"
              value={username}
              onChange={handleUsernameChange}
              className="w-full p-3 mt-1 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none"
              placeholder="Enter your username"
              required
            />
          </div>

          {/* Password Field */}
          <div>
            <label htmlFor="password" className="block text-sm font-medium text-gray-600">
              Password
            </label>
            <input
              type="password"
              id="password"
              value={password}
              onChange={handlePasswordChange}
              className="w-full p-3 mt-1 border border-gray-300 rounded-lg focus:ring-2 focus:ring-teal-500 focus:outline-none"
              placeholder="Enter your password"
              required
            />
            {passwordError && (
              <p className="mt-2 text-sm text-red-500">{passwordError}</p>
            )}
          </div>

          {/* Additional Actions */}
          <div className="flex items-center justify-between">
            <a href="/forgot-password" className="text-sm text-teal-500 hover:underline">
              Forgot Password?
            </a>
          </div>

          {/* Submit Button */}
          <div>
            <button
              type="submit"
              className="w-full px-4 py-3 text-white bg-teal-500 rounded-lg hover:bg-teal-600 focus:outline-none focus:ring-2 focus:ring-teal-500"
            >
              Sign In
            </button>
          </div>

          {/* Error Notification */}
          {error && (
            <div className="mt-4 text-sm text-red-600 text-center">
              {error}
            </div>
          )}
        </form>
      </div>
    </div>

      </>
    );
}
