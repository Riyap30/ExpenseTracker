import React, { useState, useEffect } from "react";
import '../index.css';

interface ExpenseProps {
  id: number;
  description: string;
  amount: number;
  date: Date;
  categoryName: string;
  user: number;
}

interface CategoryProps {
  id: number;
  name: string;
}

function Expense() {
  const [expenses, setExpenses] = useState<ExpenseProps[]>([]);
  const [categories, setCategories] = useState<CategoryProps[]>([]);
  const [barGraph, setBarGraph] = useState("");
  const [pieChart, setPieChart] = useState("");
  const [expense, setExpense] = useState<ExpenseProps>({
    id: 0,
    description: '',
    amount: 0,
    date: new Date(),
    categoryName: '',
    user: 0
  });
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isEditMode, setIsEditMode] = useState(false);
  const [view, setView] = useState<'bar' | 'pie'>('bar'); 
  const [openMenuId, setOpenMenuId] = useState<number | null>(null);

  useEffect(() => {
    // check if user is logged in
    if (!localStorage.getItem('token')) {
      window.location.href = '/login';
    }
    
    fetchExpenses();
    fetchCategories();
    fetchBarChart();
    fetchPieChart();
  }, []);

  // Fetch expenses
  const fetchExpenses = async () => {
    try {
      const response = await fetch('http://localhost:8081/users/expenses/me', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await response.json();
      setExpenses(data);
    } catch (error) {
      console.error('Error fetching expenses:', error);
    }
  };

  // Fetch categories
  const fetchCategories = async () => {
    try {
      const response = await fetch('http://localhost:8081/categories', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await response.json();
      setCategories(data);
    } catch (error) {
      console.error('Error fetching categories:', error);
    }
  };

  // Fetch bar chart
  const fetchBarChart = async () => {
    try {
      const response = await fetch('http://localhost:8081/expenses/category-total/bar-graph', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await response.text();
      setBarGraph(data);
    } catch (error) {
      console.error('Error fetching bar chart:', error);
    }
  };

  // Fetch pie chart
  const fetchPieChart = async () => {
    try {
      const response = await fetch('http://localhost:8081/expenses/category-total/pie-chart', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      const data = await response.text();
      setPieChart(data);
    } catch (error) {
      console.error('Error fetching pie chart:', error);
    }
  };

  // Handle add or update expense
  const handleAddOrUpdateExpense = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    // console.log(expense);
    
    const url = isEditMode 
      ? `http://localhost:8081/expenses/${expense.id}`
      : 'http://localhost:8081/expenses/';

    const method = isEditMode ? 'PUT' : 'POST';
    console.log(expense);
    try {
      const response = await fetch(url, {
        method: method,
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        },
        body: JSON.stringify(expense)
      });
      if (response.ok) {
        fetchExpenses();
        setIsModalOpen(false);
        setIsEditMode(false);
        window.location.reload();
      } else {
        console.error('Failed to save expense:', await response.json());
      }
    } catch (error) {
      console.error('Error saving expense:', error);
    }
  };

  // Handle edit expense
  const handleEditExpense = (expense: ExpenseProps) => {
    setExpense(expense);
    setIsEditMode(true);
    setIsModalOpen(true);
    toggleMenu(0);
    console.log(expense);
  };

  // Handle delete expense
  const handleDeleteExpense = async (id: number) => {
    try {
      const response = await fetch(`http://localhost:8081/expenses/${id}`, {
        method: 'DELETE',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        window.location.reload();

      } else {
        console.error('Failed to delete expense:', await response.json());
      }
    } catch (error) {
      console.error('Error deleting expense:', error);
    }
  };

  // Toggle menu
  const toggleMenu = (id: number) => {
    setOpenMenuId(openMenuId === id ? null : id);
  };

  // Reset form
  const resetForm = () => {
    setExpense({
      id: 0,
      description: '',
      amount: 0,
      date: new Date(),
      categoryName: '',
      user: 0
    });
    setIsEditMode(false);
    setIsModalOpen(true);

  };

  return (
    <div className="min-h-screen bg-gray-100 p-6">
      {/* Graph Section */}
      <div className="bg-white shadow-md rounded-lg p-4 mb-6">
        <div className="flex justify-between items-center">
          <h2 className="text-xl font-semibold mb-4">Expense Overview</h2>
          <button
            className="bg-blue-600 text-white py-2 px-4 rounded-lg hover:bg-blue-700"
            onClick={() => setView(view === 'bar' ? 'pie' : 'bar')}
          >
            {view === 'bar' ? 'Switch to Pie Chart' : 'Switch to Bar Chart'}
          </button>
        </div>
        <div className="w-3/4 mx-auto">
          {view === 'bar' ? (
            <img src={`data:image/png;base64,${barGraph}`} alt="Bar Chart" className="w-full" />
          ) : (
            <img src={`data:image/png;base64,${pieChart}`} alt="Pie Chart" className="w-full" />
          )}
        </div>
      </div>

      {/* Add Expense Button */}
      <div className="mb-6 text-right">
        <button
          className="bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700"
          onClick={resetForm}
        >
          Add Expense
        </button>
      </div>

      {/* Expense List */}
      <div className="bg-white shadow-md rounded-lg p-6">
        <h2 className="text-xl font-semibold mb-4">Your Expenses</h2>
        <ul className="space-y-4">
          {expenses.map(expense => (
            <li key={expense.id} className="flex justify-between items-center bg-gray-100 p-4 rounded-lg">
              <div className="flex space-x-4">
                <p className="font-medium">Description: {expense.description}</p>
                <p>Amount: {expense.amount}</p>
                <p>Date: {new Date(expense.date).toLocaleDateString()}</p>
                <p>Category: {expense.categoryName}</p>
              </div>
              <div className="relative">
                <button
                  className="text-gray-600 hover:text-gray-800"
                  onClick={() => toggleMenu(expense.id)}
                >
                  <svg className="w-6 h-6" fill="currentColor" viewBox="0 0 20 20">
                    <path d="M10 3a1.5 1.5 0 110-3 1.5 1.5 0 010 3zm0 7a1.5 1.5 0 110-3 1.5 1.5 0 010 3zm0 7a1.5 1.5 0 110-3 1.5 1.5 0 010 3z" />
                  </svg>
                </button>
                {openMenuId === expense.id && (
                  <div className="absolute right-0 mt-2 w-32 z-10 bg-white border border-gray-300 rounded-lg shadow-lg">
                    <button
                      className="block w-full text-left px-4 py-2 text-sm hover:bg-gray-200"
                      onClick={() => handleEditExpense(expense)}
                    >
                      Edit
                    </button>
                    <button
                      className="block w-full text-left px-4 py-2 text-sm hover:bg-gray-200"
                      onClick={() => handleDeleteExpense(expense.id)}
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

      {/* Modal for Adding/Editing Expense */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-gray-800 bg-opacity-50 flex justify-center items-center z-50">
          <div className="bg-white rounded-lg shadow-lg p-6 w-1/2">
            <h3 className="text-lg font-semibold mb-4">
              {isEditMode ? 'Edit Expense' : 'Add New Expense'}
            </h3>
            <form onSubmit={handleAddOrUpdateExpense}>
              <div className="mb-4">
                <label htmlFor="description" className="block text-gray-700 font-medium mb-2">
                  Description
                </label>
                <input
                  id="description"
                  type="text"
                  className="w-full border border-gray-300 p-2 rounded-lg"
                  value = {expense.description}
                  onChange={(e) => setExpense({ ...expense, description: e.target.value })}
                  required
                />
              </div>
              <div className="mb-4">
                <label htmlFor="amount" className="block text-gray-700 font-medium mb-2">
                  Amount
                </label>
                <input
                  id="amount"
                  type="number"
                  className="w-full border border-gray-300 p-2 rounded-lg"
                  value = {expense.amount}
                  onChange={(e) => setExpense({ ...expense, amount: parseFloat(e.target.value) })}
                  required
                />
              </div>
              <div className="mb-4">
                <label htmlFor="date" className="block text-gray-700 font-medium mb-2">
                  Date
                </label>
                <input
                  id="date"
                  type="date"
                  className="w-full border border-gray-300 p-2 rounded-lg"
                  value={new Date(expense.date).toISOString().split('T')[0]}
                  onChange={(e) => setExpense({ ...expense, date: new Date(e.target.value) })}
                  required
                />
              </div>
              <div className="mb-4">
                <label htmlFor="category" className="block text-gray-700 font-medium mb-2">
                  Category
                </label>
                <select
                  id="category"
                  className="w-full border border-gray-300 p-2 rounded-lg"
                  value={expense.categoryName}
                  onChange={(e) => setExpense({ ...expense, categoryName: e.target.value })}
                  required
                >
                  <option value="">Select Category</option>
                  {categories.map(category => (
                    <option key={category.id} value={category.name}>
                      {category.name}
                    </option>
                  ))}
                </select>
              </div>
              <div className="text-right">
                <button
                  type="button"
                  className="bg-gray-600 text-white py-2 px-4 rounded-lg hover:bg-gray-700 mr-2"
                  onClick={() => setIsModalOpen(false)}
                >
                  Cancel
                </button>
                <button
                  type="submit"
                  className="bg-green-600 text-white py-2 px-4 rounded-lg hover:bg-green-700"
                >
                  {isEditMode ? 'Update Expense' : 'Add Expense'}
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}

export default Expense;
