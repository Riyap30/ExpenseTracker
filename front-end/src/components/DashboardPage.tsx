import { useState, useEffect } from "react";
import '../index.css';
import { Link } from "react-router-dom";

interface UserData {
    username: string;
    email: string;
}

interface GroupDetails {
    id: number;
    name: string;
    date: Date;
    admin: string;
}

export default function DashboardPage() {
    const [userData, setUserData] = useState<UserData | null>(null);
    const [totalExpenses, setTotalExpenses] = useState(0);
    const [groupsDetails, setGroupsDetails] = useState<GroupDetails[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false); 
    const [newGroup, setNewGroup] = useState<GroupDetails>({
        id: 0,
        name: "",
        date: new Date(),
        admin: ""
    }); 

    useEffect(() => {
        // check if user is logged in
        if (!localStorage.getItem("token")) {
            window.location.href = "/login";
        }

        // Fetch user data
        const fetchUserData = async () => {
            try {
                const response = await fetch(`http://localhost:8081/users/me`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                setUserData(await response.json());
            } catch (error) {
                console.error("Error fetching user data:", error);
            }
        };

        // Fetch total expenses
        const fetchTotalExpenses = async () => {
            try {
                const response = await fetch(`http://localhost:8081/users/balance/me`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                const data = await response.json();
                setTotalExpenses(data.balance);
            } catch (error) {
                console.error("Error fetching total expenses:", error);
            }
        };


        // Fetch group details
        const fetchGroupDetails = async () => {
            try {
                const response = await fetch(`http://localhost:8081/groups/me`, {
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                const data = await response.json();
                setGroupsDetails(data);
            } catch (error) {
                console.error("Error fetching group details:", error);
            }
        };

        fetchUserData();
        fetchTotalExpenses();
        fetchGroupDetails();
    }, []);

    // Add Group
    const handleAddGroup = async () => {
        try {
            const response = await fetch(`http://localhost:8081/groups/`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify(newGroup)  
            });

            if (response.ok) {
                setIsModalOpen(false);
                window.location.reload(); 
            }
        } catch (error) {
            console.error("Error adding group:", error);
        }
    };

    return (
        <div className="min-h-screen bg-gray-100 p-6">
            {/* Welcome Message */}
            <div className="text-2xl font-semibold mb-4">
                Welcome, {userData?.username || 'User'}!
            </div>

            {/* User Data Summary */}
            <div className="bg-white shadow-lg rounded-lg p-6">
                <h2 className="text-2xl font-semibold mb-6 text-gray-800">Your Summary</h2>

                <div className="grid grid-cols-2 gap-6">
                    {/* Total Expenses Card */}
                    <div className="bg-teal-200 p-6 rounded-lg text-center hover:bg-teal-300 transition ease-in-out duration-200">
                        <Link to="/expenses">
                            <h3 className="text-lg font-medium text-gray-700">Total Expenses</h3>
                            <p className="text-3xl font-bold text-gray-900">
                                {"$" + totalExpenses || 0}
                            </p>
                        </Link>
                    </div>

                    {/* Group Details Card */}
                    <div className="bg-gray-700 p-6 rounded-lg text-center hover:bg-gray-600 transition ease-in-out duration-200">
                        <h3 className="text-lg font-medium text-white">Group Details</h3>
                        {groupsDetails && groupsDetails.length > 0 ? (
                            <ul className="mt-2">
                                {groupsDetails.map((group) => (
                                    <li key={group.id} className="mb-2 text-sm font-medium text-gray-200">
                                        <a
                                            href={`${group.id}/group`}
                                            className="hover:underline hover:text-gray-300"
                                        >
                                            {group.name}
                                        </a>
                                    </li>
                                ))}
                            </ul>
                        ) : (
                            <p className="text-sm text-gray-400">No groups found.</p>
                        )}
                    </div>

                    {/* Add Group Button */}
                    <div className="col-span-2 flex justify-center">
                        <button
                            className="bg-teal-600 text-white py-3 px-6 rounded-lg hover:bg-teal-700 transition ease-in-out duration-200"
                            onClick={() => setIsModalOpen(true)}
                        >
                            Add Group
                        </button>
                    </div>
                </div>
            </div>

            {/* Modal for Adding Group */}
            {isModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                        <h2 className="text-xl font-semibold mb-4">Add New Group</h2>
                        <input
                            type="text"
                            value={newGroup.name}
                            onChange={(e) => setNewGroup({ ...newGroup, name: e.target.value })}
                            placeholder="Group Name"
                            className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
                        />
                        <input
                            type="date"
                            defaultValue={new Date(newGroup.date).toISOString().split("T")[0]}
                            readOnly
                            className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
                        />
                        <div className="text-right mt-4">
                            <button
                                className="bg-gray-500 text-white py-2 px-4 rounded-lg hover:bg-gray-600 mr-2"
                                onClick={() => setIsModalOpen(false)}
                            >
                                Cancel
                            </button>
                            <button
                                className="bg-teal-600 text-white py-2 px-4 rounded-lg hover:bg-teal-700"
                                onClick={handleAddGroup}
                            >
                                Add Group
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>

    );
}
