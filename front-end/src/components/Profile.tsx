import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';

interface UserInterface {
    username?: string;
    email?: string;
    password?: string;
    newPassword?: string;
    confirmPassword?: string;
}

function Profile() {
    const { logout } = useAuth();
    const [user, setUser] = useState<UserInterface>({
        username: '',
        email: '',
    });

    const [passwordChange, setPasswordChange] = useState<UserInterface>({
        username: '',
        email: '',
        password: '',
        newPassword: '',
        confirmPassword: '',
    });

    useEffect(() => {
        // check if user is logged in
        if (!localStorage.getItem('token')) {
             window.location.href = '/login';
        }

        // Fetch user
        const fetchUser = async () => {
            try {
                const response = await fetch('http://localhost:8081/users/me', {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json',
                        'Authorization': `Bearer ${localStorage.getItem('token')}`
                    }
                });

                const data = await response.json();
                
                setUser(data);
                console.log(data);
            } catch (error) {
                console.error('Error fetching user:', error);
            }
        };

        fetchUser();
    }, []);

    // Update user
    const handleUpdate = async (event: React.FormEvent<HTMLFormElement>) => {
        event.preventDefault();

        const completeUser = {
            username: user.username,
            email: user.email,
            password: ""
        }
        console.log(completeUser);
        try {
            const response = await fetch('http://localhost:8081/users/me', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(completeUser)
            });

            const data = await response.json();

            console.log(data);
            localStorage.setItem("update", "Username and Email");
            localStorage.setItem("isUpdate", "true");
            logout();

        } catch (error) {
            console.error('Error updating user:', error);
        }
    };

    // Update password
    const handleChangePassword = async (event: React.ChangeEvent<HTMLFormElement>) => {
        event.preventDefault();

        if (passwordChange.newPassword !== passwordChange.confirmPassword) {
            alert('Passwords do not match');
            return;
        }
        console.log(user);
        const completeUser = {
            username: user.username,
            email: user.email,
            password: passwordChange.newPassword
        }
        console.log(completeUser);
        try {
            const response = await fetch('http://localhost:8081/users/me', {
                method: 'PUT',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify({...user, password: passwordChange.newPassword})
            });

            const data = await response.json();

            console.log(data);
            localStorage.setItem("update", "Password");
            localStorage.setItem("isUpdate", "true");
            logout();

        } catch (error) {
            console.error('Error updating user:', error);

        }

    };

    return (
        <div className="min-h-screen bg-gray-100 p-6">
        <div className="text-2xl font-semibold mb-4">Profile</div>
  
        <div className="bg-white shadow-md rounded-lg p-6 mb-6">
          <h2 className="text-xl font-semibold mb-4">Update Profile</h2>
          <form onSubmit={handleUpdate} className="space-y-4">
            <div>
              <label className="block text-lg font-medium text-gray-700">
                Username:
              </label>
              <input
                type="text"
                value={user.username}
                onChange={(event) => setUser({ ...user, username: event.target.value })}
                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
              />
            </div>
            <div>
              <label className="block text-lg font-medium text-gray-700">
                Email:
              </label>
              <input
                type="email"
                value={user.email}
                onChange={(event) => setUser({ ...user, email: event.target.value })}
                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
              />
            </div>
            <button
              type="submit"
              className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
            >
              Update
            </button>
          </form>
        </div>
  
        <div className="bg-white shadow-md rounded-lg p-6">
          <h2 className="text-xl font-semibold mb-4">Change Password</h2>
          <form onSubmit={handleChangePassword} className="space-y-4">
            <div>
              <label className="block text-lg font-medium text-gray-700">
                Old Password:
              </label>
              <input
                type="password"
                value={passwordChange.password}
                onChange={(event) => setPasswordChange({ ...passwordChange, password: event.target.value })}
                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
              />
            </div>
            <div>
              <label className="block text-lg font-medium text-gray-700">
                New Password:
              </label>
              <input
                type="password"
                value={passwordChange.newPassword}
                onChange={(event) => setPasswordChange({ ...passwordChange, newPassword: event.target.value })}
                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
              />
            </div>
            <div>
              <label className="block text-lg font-medium text-gray-700">
                Confirm Password:
              </label>
              <input
                type="password"
                value={passwordChange.confirmPassword}
                onChange={(event) => setPasswordChange({ ...passwordChange, confirmPassword: event.target.value })}
                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
              />
            </div>
            <button
              type="submit"
              className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
            >
              Change Password
            </button>
          </form>
        </div>
      </div>
    );
}

export default Profile;