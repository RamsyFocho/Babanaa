// Add logout function
function logout() {
    localStorage.removeItem('jwt_token');
    localStorage.removeItem('isLoggedIn');
    localStorage.removeItem('agentData');
    window.location.href = '/login';
}