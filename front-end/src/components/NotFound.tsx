import { useNavigate } from 'react-router-dom';

function NotFoundPage(){
  const navigate = useNavigate();

  const redirectToDashboard = () => {
    navigate('/home'); 
  };

  return (
    <div className="flex items-center justify-center h-screen bg-gray-100">
      <div className="text-center">
        <h1 className="text-6xl font-bold text-gray-800 mb-4">404</h1>
        <p className="text-2xl text-gray-600 mb-8">Oops! The page you're looking for does not exist.</p>
        <button
          onClick={redirectToDashboard}
          className="bg-blue-500 text-white font-bold py-2 px-4 rounded hover:bg-blue-600 transition duration-300"
        >
          Go to Dashboard
        </button>
      </div>
    </div>
  );
};

export default NotFoundPage;
