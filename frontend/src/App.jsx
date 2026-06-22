import HomePage from './pages/HomePage';
import './App.css';

// Version simplificada para AWS/EKS:
// no usa auth-service ni api-gateway. El frontend consume ddaa-service mediante /api.
function App() {
  const demoUser = {
    name: 'Modo demo AWS/EKS',
    email: 'demo@ddaa.local'
  };

  return <HomePage user={demoUser} />;
}

export default App;
