// Add event listener for storage changes (for multi-tab support)
window.addEventListener('storage', (e) => {
    if (e.key === 'jwt_token' && !e.newValue) {
        // Token was removed in another tab
        window.location.href = '/login';
    }
});