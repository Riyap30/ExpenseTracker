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
   <div className="w-full max-w-md p-8 space-y-6 bg-white rounded shadow-lg">
     <h2 className="text-3xl font-bold text-center text-teal-500">Create Your Account</h2>
     <form onSubmit={handleSubmit} className="space-y-5">
       {/* Username Input */}
       <div>
         <label
           htmlFor="name"
           className="block text-sm font-medium text-gray-700"
         >
           Username
         </label>
         <input
           type="text"
           id="name"
           name="name"
           value={formData.username}
           onChange={handleUsernameChange}
           className="w-full p-3 mt-1 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-teal-500"
           placeholder="Enter your username"
           required
         />
       </div>

       {/* Email Input */}
       <div>
         <label
           htmlFor="email"
           className="block text-sm font-medium text-gray-700"
         >
           Email
         </label>
         <input
           type="email"
           id="email"
           name="email"
           value={formData.email}
           onChange={handleEmailChange}
           className="w-full p-3 mt-1 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-teal-500"
           placeholder="Enter your email address"
           required
         />
       </div>

       {/* Password Input */}
       <div>
         <label
           htmlFor="password"
           className="block text-sm font-medium text-gray-700"
         >
           Password
         </label>
         <input
           type="password"
           id="password"
           name="password"
           value={formData.password}
           onChange={handlePasswordChange}
           className="w-full p-3 mt-1 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-teal-500"
           placeholder="Create a strong password"
           required
         />
         {passwordError && (
           <p className="text-red-500 mt-2 text-sm">{passwordError}</p>
         )}
       </div>

       {/* Message */}
       {message && (
         <div
           className={`text-sm text-center ${
             isSuccess ? 'text-green-500' : 'text-red-500'
           }`}
         >
           {message}
         </div>
       )}

       {/* Submit Button */}
       <div className="flex justify-center">
         <button
           type="submit"
           className="w-full px-4 py-3 text-white rounded bg-teal-500 hover:bg-teal-600 transition"
         >
           Register
         </button>
       </div>
     </form>
     {/* Footer */}
     <div className="text-center text-sm text-gray-600">
       Already have an account?{' '}
       <a
         href="/login"
         className="text-teal-500 hover:underline transition"
       >
         Login
       </a>
     </div>
   </div>
 </div>
);

}

export default RegisterForm;