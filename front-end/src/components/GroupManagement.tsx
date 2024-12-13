
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';


interface Group {
    id: number;
    name: string;
    date: Date;
    admin: string;
    adminName: string;
}

interface Split {
    id: number;
    amount: number;
    groupId: number;
    payerId: number;
    payerName: string;
    payeeId: number;
    payeeName: string;
    categoryName: string;
    settled: boolean;
    status: string;
}

interface Settlement {
    id: number;
    amount: number;
    settledDate: Date;
    groupId: number;
    payerId: number;
    payerName: string;
    receiverId: number;
    receiverName: string;
}

interface User {
    id: number;
    username: string;
    email: string;
}

interface Category {
    id: number;
    name: string;
}



function GroupManagement() {

    const { groupId } = useParams();

    const [splits, setSplits] = useState<Split[]>([]);
    const [settlements, setSettlements] = useState<Settlement[]>([]);
    const [searchUser, setSearchUser] = useState<User[]>([]);
    const [groupUsers, setGroupUsers] = useState<User[]>([]);
    const [selectedUser, setSelectedUser] = useState<User>({
        id: 0,
        username: "",
        email: ""
    });
    
    const [group, setGroup] = useState<Group>({
        id: 0,
        name: "",
        date: new Date(),
        admin: "",
        adminName: ""
    });
    const [categorys, setCategories] = useState<Category[]>([]);
    const [isSplitModalOpen, setIsSplitModalOpen] = useState(false);
    const [newSplit,  setNewSplit] = useState<Split>({
        id: 0,
        amount: 0,
        groupId: 0,
        payerId: 0,
        payerName: "",
        payeeId: 0,
        payeeName: "",
        categoryName: "",
        settled: false,
        status: ""
    });
    
    const [isEditGroupModalOpen,setIsEditGroupModalOpen] = useState(false);
    const [user, setUser] = useState<User[]>([]);
    const [isModalOpen, setIsModalOpen] = useState(false);
    const [activeTab, setActiveTab] = useState(localStorage.getItem("update") || "splits");
    const [isEditSplitModalOpen, setIsEditSplitModalOpen] = useState(false);
    const [openMenuId, setOpenMenuId] = useState<number | null>(null);
    const [newGroup, setNewGroup] = useState<Group>({
        id: 0,
        name: "",
        date: new Date(),
        admin: "",
        adminName: ""
    })


    useEffect(() => {

        // check if user is logged in
        if (!localStorage.getItem("token")) {
            window.location.href = "/login";
        }

        console.log(groupId);
        localStorage.setItem("update", "");
        // Fetch Group Details
        const fetchGroup = async () => {
            try{
                const groupDetails = await fetch(`http://localhost:8081/groups/${groupId}`,{
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                const data = await groupDetails.json();
                setGroup(data);
                console.log(data);
            } catch (error) {
                console.error("Error fetching group details:", error);
            }
        };

        // Fetch Splits
        const fetchSplits = async () => {
            try{
                const splitsData = await fetch(`http://localhost:8081/splits/group/${groupId}`,{
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                    
                });
                const data = await splitsData.json();
                setSplits(data);
                console.log(data);
            } catch (error) {
                console.error("Error fetching group details:", error);
            }
        };

        // Fetch Settlements
        const fetchSettlements = async () => {
            try{
                const settlements = await fetch(`http://localhost:8081/settlements/groups/${groupId}`,{
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                const data = await settlements.json();
                setSettlements(data);
                console.log(data);
            } catch (error) {
                console.error("Error fetching group details:", error);
            }
        };

        // Fetch User
        const fetchUser = async () => {
            try{
                const user = await fetch(`http://localhost:8081/groups/${groupId}/users`,{
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                const data = await user.json();
                setUser(data);
                console.log(data);
            } catch (error) {
                console.error("Error fetching group details:", error);
            }
        };

        // Fetch Categories
        const fetchCategories = async () => {
            try{
                const categories = await fetch(`http://localhost:8081/categories`,{
                    method: "GET",
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("token")}`
                    }
                });
                const data = await categories.json();
                setCategories(data);
                console.log(data);
            } catch (error) {
                console.error("Error fetching group details:", error);
            }
        };

        fetchGroup();
        fetchSplits();
        fetchSettlements();
        fetchUser();
        fetchCategories();
        searchUsers();
        getGroupUsers();
    }, []);

    // Toggle menu
    const toggleMenu = (id: number) => {
        setOpenMenuId(openMenuId === id ? null : id);
      };

    // Fetch Group Users
    const getGroupUsers = async () => {
        try{
            const response = await fetch(`http://localhost:8081/groups/${groupId}/users`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                }
            });
            const data = await response.json();
            setGroupUsers(data);
            console.log(data);
        } catch (error) {
            console.error("Error fetching group details:", error);
    }
        
    }
    
    // Search Users
    const searchUsers = async () => {
        try{
            const response = await fetch(`http://localhost:8081/users/search/all`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                }
            });
            const data = await response.json();
            setSearchUser(data);
            console.log(data);
        } catch (error) {
            console.error("Error fetching group details:", error);
        }

    };

    // Add Split
    const addSplit = async () => {
        
    if (!group) {
        return <div>Loading...</div>;

    }

    const response = await fetch(`http://localhost:8081/splits/${groupId}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
            "Authorization": `Bearer ${localStorage.getItem("token")}`
        },
        body: JSON.stringify(newSplit),
    });

    if (response.ok) {
        localStorage.setItem("update", "splits");
        window.location.reload();
    } else if (response.status === 400) {
        alert("Same payer and payee cannot be selected");
    }


    };  

    // Add Settlement
    const addSettlement = async (split: Split, event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        if (!group) {
            return <div>Loading...</div>;
    
        }

        const settlement = {
            amount: split.amount,
            settledDate: new Date().toISOString().split('T')[0],
            groupId: groupId !== undefined? parseInt(groupId): 0,
            receiverId: split.payerId,
            receiverName: split.payerName,
            payerId: split.payeeId,
            payerName: split.payeeName

        }
        console.log(settlement)

        const resposne = await fetch(`http://localhost:8081/settlements/${split.id}`, {   
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": `Bearer ${localStorage.getItem("token")}`
            },
            body: JSON.stringify(settlement),
        });

        if (resposne.ok) {
            localStorage.setItem("update", "settlements");
            window.location.reload();
        } else if (resposne.status === 401) {
            toggleMenu(0);
            alert("Not a part of the split");
        }
    }

    // Tab Change
    const handleTabChange = (tab: string) => {
        setActiveTab(tab);
    }

    // User Select
    const handleUserSelect = (event) => {
        setSelectedUser(event.target.value);
    };

    // Handle Input Change
    const handleInputChange = (event) => {
        setNewSplit({
            ...newSplit,
            [event.target.name]: event.target.value,
        });
    }

    // Add User to Group
    const addUser = async () => {
        console.log(selectedUser)
        try{
            const response = await fetch(`http://localhost:8081/groups/${groupId}/users/${selectedUser}`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify({userId: selectedUser?.id}),
            });
            if (response.ok) {
                localStorage.setItem("update", "users");
                window.location.reload();
            } else if (response.status === 400) {
                alert("User already added to group");
            } else if (response.status === 401) {
                alert("Unauthorized");
            }
        } catch (error) {
            console.error("Error fetching group details:", error);
        }
    }

    // Remove User from Group
    const removeUser = async (selectedUser: User, event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        try{
            const response = await fetch(`http://localhost:8081/groups/${groupId}/users/${selectedUser?.id}`, {
                method: "DELETE",
                headers: {
                    "Content-Type": "application/json",
                    "Authorization": `Bearer ${localStorage.getItem("token")}`
                },
                body: JSON.stringify({userId: selectedUser?.id}),
            });
            if (response.ok) {
                window.location.reload();
            } else if (response.status === 400) {
                alert("User is the admin of the group");
            } else if (response.status === 401) {
                alert("Unauthorized");
            }
        } catch (error) {
            console.error("Error fetching group details:", error);
        }
    }

    // Edit Split
    const handleEditSplit = async (split: Split, event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        try{
            const response = await fetch(`http://localhost:8081/splits/${split.id}`, {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(split)
            });
            if (response.ok){
                setIsEditSplitModalOpen(false);
                window.location.reload();
            } else if (response.status === 401){
                toggleMenu(0);
                alert("Not a part of the split");
            } else if (response.status === 400){
                toggleMenu(0);
                alert("Same payer and payee cannot be selected");
            }
            else {
                console.error("Error editing split:", await response.json());
            }
        } catch (error){
            console.log(error);
        }
    }

    // Delete Split
    const handleDeleteSplit = async (split: Split, event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        try{
            const response = await fetch(`http://localhost:8081/splits/${split.id}`, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            if (response.ok){
                window.location.reload();
            } else if(response.status === 400){
                toggleMenu(0);
                alert("Not a part of the split");
            }else if(response.status === 401){
                toggleMenu(0);
                alert("Unauthorized");
            } else {
                console.error("Error deleting split:", await response.json());
            }
        } catch (error){
            console.log(error);
        }
    }

    // Open Edit Modal
    const openEditModal = (split: Split) => {
        toggleMenu(0);
        setNewSplit(split);
        setIsEditSplitModalOpen(true);
    }
    
    
    // Edit Group
    const handleUpdateGroup = async (group: Group, event: React.MouseEvent<HTMLButtonElement, MouseEvent>) => {
        event.preventDefault();
        console.log(group);
        
        try{
            const response = await fetch(`http://localhost:8081/groups/${newGroup.id}`, {
                method: "PUT",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                },
                body: JSON.stringify(newGroup)
            });
            if (response.ok){
                setIsEditGroupModalOpen(false);
                window.location.reload();
            } else if (response.status === 401) {
                alert("Unauthorized");
            } else {
                console.error("Error editing group:", await response.json());
            }
        } catch (error){
            console.log(error);
        }
    }

    // Delete Group
    const handleDeleteGroup = async () => {
        console.log("delete group");
        try{
            const response = await fetch(`http://localhost:8081/groups/${groupId}`, {
                method: "DELETE",
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': `Bearer ${localStorage.getItem('token')}`
                }
            });
            if (response.ok){
                window.location.href = "/home";
            } else if (response.status === 401) {
                alert("Unauthorized");
            } else {
                console.error("Error deleting group:", await response.json());
            }
        } catch (error){
            console.log(error);
        }
    }

  

    return (
        <div className="min-h-screen bg-gray-100 p-6">
            {/* Group Information */}
            <div className="bg-white shadow-lg rounded-lg p-6 mb-6">
                <h1 className="text-3xl font-bold text-gray-800 mb-2">{group?.name}</h1>
                <p className="text-gray-600 mb-4">Admin: <span className="font-medium text-gray-800">{group.adminName}</span></p>
                <p className="text-gray-600 mb-4">Date Created: <span className="font-medium text-gray-800">{new Date(group.date).toLocaleDateString()}</span></p>
                
                <div className="flex flex-wrap gap-4">
                    <button
                        className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors duration-300"
                        onClick={() => setIsModalOpen(true)}
                    >
                        Add User
                    </button>
                    <button
                        className="flex-1 bg-yellow-500 text-white py-2 px-4 rounded-lg hover:bg-yellow-600 transition-colors duration-300"
                        onClick={() => {setIsEditGroupModalOpen(true); setNewGroup(group);}}
                    >
                        Edit Group
                    </button>
                    <button
                        className="flex-1 bg-red-600 text-white py-2 px-4 rounded-lg hover:bg-red-700 transition-colors duration-300"
                        onClick={handleDeleteGroup}
                    >
                        Delete Group
                    </button>
                </div>
            </div>


            {/* Tabs */}
            <div className="bg-white shadow-lg rounded-lg p-6">
            {/* Tabs */}
            <div className="flex space-x-4 mb-6 border-b border-gray-300">
                <button
                    className={`px-4 py-2 rounded-lg font-medium transition-colors ${activeTab === 'splits' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}`}
                    onClick={() => handleTabChange('splits')}
                >
                    Splits
                </button>
                <button
                    className={`px-4 py-2 rounded-lg font-medium transition-colors ${activeTab === 'settlements' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}`}
                    onClick={() => handleTabChange('settlements')}
                >
                    Settlements
                </button>
                <button
                    className={`px-4 py-2 rounded-lg font-medium transition-colors ${activeTab === 'users' ? 'bg-blue-600 text-white' : 'bg-gray-200 text-gray-700'}`}
                    onClick={() => handleTabChange('users')}
                >
                    Users
                </button>
            </div>

            {/* Tab Content */}
            {activeTab === 'splits' && (
                <div>
                    <button
                        className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700 transition-colors mb-4"
                        onClick={() => setIsSplitModalOpen(true)}
                    >
                        Add Split
                    </button>
                    <ul>
                        {splits.filter(split => !split.settled).map((split) => (
                            <li key={split.id} className="flex justify-between items-center p-4 border-b border-gray-300 hover:bg-gray-50 transition-colors">
                                <div className="flex flex-col md:flex-row md:items-center md:space-x-4">
                                    <span className="font-semibold text-lg">Amount: ${split.amount}</span>
                                    <span className="text-gray-600">Payer: {split.payerName}</span>
                                    <span className="text-gray-600">Payee: {split.payeeName}</span>
                                    <span className="text-gray-600">Category: {split.categoryName}</span>
                                </div>
                                
                                <div className="relative">
                                    <button
                                        className="text-gray-600 hover:text-gray-900 transition-colors focus:outline-none"
                                        onClick={() => toggleMenu(split.id)}
                                        
                                    >
                                        ⋮
                                    </button>
                                    {openMenuId === split.id && (
                                        <div className="absolute right-0 z-10 mt-2 w-48 bg-white border border-gray-300 rounded-lg shadow-lg">
                                            <button
                                                className="block w-full text-left px-4 py-2 hover:bg-gray-100 transition-colors"
                                                onClick={(event) => addSettlement(split, event)}
                                            >
                                                Settle
                                            </button>
                                            <button
                                                className="block w-full text-left px-4 py-2 hover:bg-gray-100 transition-colors"
                                                onClick={() => openEditModal(split)}
                                            >
                                                Edit
                                            </button>
                                            <button
                                                className="block w-full text-left px-4 py-2 hover:bg-gray-100 transition-colors"
                                                onClick={(event) => handleDeleteSplit(split, event)}
                                            >
                                                Delete
                                            </button>
                                        </div>
                                    )}
                                </div>
                            </li>
                        ))}
                    </ul>
                </div>
            )}

            {activeTab === 'settlements' && (
                <ul>
                    {settlements.map((settlement) => (
                        <li key={settlement.id} className="flex justify-between items-center p-4 border-b border-gray-300 hover:bg-gray-50 transition-colors">
                            <div className="flex flex-col md:flex-row md:items-center md:space-x-4">
                                <span className="font-semibold text-lg">Amount: ${settlement.amount}</span>
                                <span className="text-gray-600">Settled Date: {new Date(settlement.settledDate).toLocaleDateString()}</span>
                                <span className="text-gray-600">Payer ID: {settlement.payerName}</span>
                                <span className="text-gray-600">Receiver ID: {settlement.receiverName}</span>
                            </div>
                        </li>
                    ))}
                </ul>
            )}

            {activeTab === 'users' && (
                <ul>
                    {user.map((user) => (
                        <li key={user.id} className="flex justify-between items-center p-4 border-b border-gray-300 hover:bg-gray-50 transition-colors">
                            <span className="font-semibold text-lg">{user.username}</span>
                            <button
                                className="bg-red-600 text-white py-2 px-4 rounded-lg hover:bg-red-700 transition-colors focus:outline-none"
                                onClick={(event) => removeUser(user, event)}
                            >
                                Delete
                            </button>
                        </li>
                    ))}
                </ul>
            )}
        </div>

            {/* Modal */}
            {isModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                        <h2 className="text-xl font-semibold mb-4">Search and Add User</h2>
                        
                        <select
                            className="block w-full p-2 border border-gray-300 rounded-lg"
                            value={selectedUser.id}
                            onChange={handleUserSelect}
                        >
                            <option value="">Select a user</option>
                            {searchUser.map(user => (
                                <option key={user.id} value={user.id}>
                                    {user.username}
                                </option>
                            ))}
                        </select>

                        <div className="text-right mt-4">
                            <button
                                className="bg-gray-500 text-white py-2 px-4 rounded-lg hover:bg-gray-600 mr-2"
                                onClick={() => setIsModalOpen(false)}
                            >
                                Cancel
                            </button>
                            <button
                                className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
                                onClick={addUser}
                                disabled={!selectedUser}
                            >
                                Add User
                            </button>
                        </div>
                    </div>
                </div>
            )}
             {isSplitModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                        <h2 className="text-xl font-semibold mb-4">Add New Split</h2>

                        <div className="mb-4">
                            <label className="block text-gray-700">Amount:</label>
                            <input
                                type="number"
                                name="amount"
                                value={newSplit.amount}
                                onChange={handleInputChange}
                                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700">Payer:</label>
                            <select
                                name="payerId"
                                value={newSplit.payerId}
                                onChange={handleInputChange}
                                className="block w-full p-2 border border-gray-300 rounded-lg"
                                required
                            >
                                <option value="">Select Payer</option>
                                {groupUsers.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {user.username}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700">Payee:</label>
                            <select
                                name="payeeId"
                                value={newSplit.payeeId}
                                onChange={handleInputChange}
                                className="block w-full p-2 border border-gray-300 rounded-lg"
                                required
                            >
                                <option value="">Select Payee</option>
                                {groupUsers.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {user.username}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700">Category:</label>
                            <select
                                name="categoryName"
                                value={newSplit.categoryName}
                                onChange={handleInputChange}
                                className="block w-full p-2 border border-gray-300 rounded-lg"
                                required
                            >
                                <option value="">Select Category</option>
                                {categorys.map(category => (
                                    <option key={category.id} value={category.name}>
                                        {category.name}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="text-right mt-4">
                            <button
                                className="bg-gray-500 text-white py-2 px-4 rounded-lg hover:bg-gray-600 mr-2"
                                onClick={() => setIsSplitModalOpen(false)}
                            >
                                Cancel
                            </button>
                            <button
                                className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
                                onClick={addSplit}
                                disabled={!newSplit.amount || !newSplit.payerId || !newSplit.payeeId || !newSplit.categoryName}
                            >
                                Add Split
                            </button>
                        </div>
                    </div>
                </div>
            )}
             {isEditSplitModalOpen && (
                <div className="fixed inset-0 flex items-center justify-center bg-black bg-opacity-50">
                    <div className="bg-white p-6 rounded-lg shadow-lg w-full max-w-md">
                        <h2 className="text-xl font-semibold mb-4">Edit Split</h2>

                        <div className="mb-4">
                            <label className="block text-gray-700">Amount:</label>
                            <input
                                type="number"
                                name="amount"
                                value={newSplit.amount}
                                onChange={handleInputChange}
                                className="mt-1 p-2 border border-gray-300 rounded-lg w-full"
                                required
                            />
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700">Payer:</label>
                            <select
                                name="payerId"
                                value={newSplit.payerId}
                                onChange={handleInputChange}
                                className="block w-full p-2 border border-gray-300 rounded-lg"
                                required
                            >
                                <option value="">Select Payer</option>
                                {groupUsers.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {user.username}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700">Payee:</label>
                            <select
                                name="payeeId"
                                value={newSplit.payeeId}
                                onChange={handleInputChange}
                                className="block w-full p-2 border border-gray-300 rounded-lg"
                                required
                            >
                                <option value="">Select Payee</option>
                                {groupUsers.map(user => (
                                    <option key={user.id} value={user.id}>
                                        {user.username}
                                    </option>
                                ))}
                            </select>
                        </div>

                        <div className="mb-4">
                            <label className="block text-gray-700">Category:</label>
                            <select
                                name="categoryName"
                                value={newSplit.categoryName}
                                onChange={handleInputChange}
                                className="block w-full p-2 border border-gray-300 rounded-lg"
                                required
                            >
                                <option value="">Select Category</option>
                                {categorys.map(category => (
                                    <option key={category.id} value={category.name}>
                                        {category.name}
                                    </option>
                                ))}
                            </select>
                        </div>


                        <div className="text-right mt-4">
                            <button
                                className="bg-gray-500 text-white py-2 px-4 rounded-lg hover:bg-gray-600 mr-2"
                                onClick={() => setIsEditSplitModalOpen(false)}
                            >
                                Cancel
                            </button>
                            <button
                                className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
                                onClick={(event) => handleEditSplit(newSplit, event)}
                                disabled={!newSplit.amount || !newSplit.payerId || !newSplit.payeeId || !newSplit.categoryName}
                            >
                                Update Split
                            </button>
                        </div>
                    </div>
                </div>
            )}
            {isEditGroupModalOpen && (
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
                          type = "date"
                          defaultValue = {new Date(newGroup.date).toISOString().split("T")[0]}
                          readOnly
                          className = "mt-1 p-2 border border-gray-300 rounded-lg w-full"
                        />
                        <div className="text-right mt-4">
                            <button
                                className="bg-gray-500 text-white py-2 px-4 rounded-lg hover:bg-gray-600 mr-2"
                                onClick={() => setIsEditGroupModalOpen(false)}
                            >
                                Cancel
                            </button>
                            <button
                                className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
                                onClick={(event) => handleUpdateGroup(group, event)}
                            >
                                Update Group
                            </button>
                        </div>
                    </div>
                </div>
            )}
        </div>
    );

}

export default GroupManagement;    