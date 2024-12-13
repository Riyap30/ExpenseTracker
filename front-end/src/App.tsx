import React from "react";
import { BrowserRouter as Router, Route, Routes} from "react-router-dom";
import Login from "./components/Login";
import RegisterForm from "./components/RegisterForm";
// import DashboardPage from "./components/DashboardPage";
// import GroupManagement from "./components/GroupManagement";
// import Expense from "./components/Expense";
// import Profile from "./components/Profile";
import AuthProvider from "./context/AuthContext";
import Footer from "./navbar/Footer";
import Header from "./navbar/Header";
import DashboardPage from "./components/DashboardPage";
import './index.css';
import GroupManagement from "./components/GroupManagement";
import Expense from "./components/Expense";
import Profile from "./components/Profile";
import NotFoundPage from "./components/NotFound";

const App: React.FC = () => {
  return (
    <Router>
      <AuthProvider>
        <Header />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<RegisterForm />} />
        
        <Route path="/home" element={<DashboardPage />} />
        <Route path="/:groupId/group" element={<GroupManagement />} />
        <Route path="/expenses" element={<Expense />} />
        <Route path="/profile" element={<Profile />} />
        <Route path="*" element={<NotFoundPage />} />
      </Routes>
      <Footer/>
      </AuthProvider>
    </Router>
  )	
};

export default App;