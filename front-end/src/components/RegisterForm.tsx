import React, { useState } from 'react';


interface RegistrationProps {
    username: string,
    email: string,
    password: string
}

function RegisterForm() { 
    const [formData, setFormData] = useState<RegistrationProps>({
        username: '',
        email: '',
        password: ''
    });

    const [passwordError, setPasswordError] = useState<string | null>(null);
    const [message, setMessage] = useState<string | null>(null);
    const [isSuccess, setIsSuccess] = useState<boolean | null>(null);

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
        setFormData(prevState => ({...prevState, username: e.target.value}));
    };

    // Handle form input changes
    const handleEmailChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        setFormData(prevState => ({...prevState, email: e.target.value}));
    };

    // Handle form input changes
    const handlePasswordChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const newPassword = e.target.value;
        setFormData(prevState => ({...prevState, password: newPassword}));

        const error = validatePassword(newPassword);
        setPasswordError(error);
    };

    // Handle form submission
    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();

        const response = await fetch(`http://localhost:8081/auth/register`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(formData)
        });

        if (response.ok) {
            setIsSuccess(true);
            setMessage("Registration successful. Please log in.");
            setFormData({
                username: '',
                email: '',
                password: ''
            });
            window.location.href = '/login';

        } else {
            setIsSuccess(false);
            setMessage("Registration failed. Please try again.");
        }
    };

    return (
        <div className="flex items-center justify-center min-h-screen bg-gray-100">
      <div className="w-full max-w-md p-8 space-y-4 bg-white rounded shadow-lg">
        <h2 className="text-2xl font-bold text-center">Register</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label htmlFor="name" className="block text-sm font-medium text-gray-700">
              Username
            </label>
            <input
              type="text"
              id="name"
              name="name"
              value={formData.username}
              onChange={handleUsernameChange}
              className="w-full p-2 mt-1 border border-gray-300 rounded"
              required
            />
          </div>
          <div>
            <label htmlFor="email" className="block text-sm font-medium text-gray-700">
              Email
            </label>
            <input
              type="email"
              id="email"
              name="email"
              value={formData.email}
              onChange={handleEmailChange}
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
              name="password"
              value={formData.password}
              onChange={handlePasswordChange}
              className="w-full p-2 mt-1 border border-gray-300 rounded"
              required
            />
            {passwordError && <p className="text-red-500 mt-1">{passwordError}</p>}
          </div>
          {message && (
            <div className={`text-sm ${isSuccess ? 'text-green-500' : 'text-red-500'}`}>
              {message}
            </div>
          )}
          <div className="flex justify-center">
            <button
              type="submit"
              className="px-4 py-2 text-white bg-blue-600 rounded hover:bg-blue-700"
            >
              Register
            </button>
            
          </div>
        </form>
      </div>
    </div>
  );

}

export default RegisterForm;