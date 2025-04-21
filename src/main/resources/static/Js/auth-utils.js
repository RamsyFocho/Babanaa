// Authentication utility functions
const AuthUtils = {
  getToken() {
    return localStorage.getItem("jwt_token");
  },

  isAuthenticated() {
    return !!this.getToken();
  },

  setToken(token) {
    localStorage.setItem("jwt_token", token);
    localStorage.setItem("isLoggedIn", "true");
    this.addAuthorizationHeader(token);
  },

  clearToken() {
    localStorage.removeItem("jwt_token");
    localStorage.removeItem("isLoggedIn");
    localStorage.removeItem("agentData");
  },

  addAuthorizationHeader(token) {
    const originalFetch = window.fetch;
    window.fetch = function () {
      let [resource, config] = arguments;
      if (!config) config = {};
      if (!config.headers) config.headers = {};
      config.headers["Authorization"] = `Bearer ${token}`;
      return originalFetch(resource, config);
    };
  },

  handleUnauthorized() {
    this.clearToken();
    window.location.href = "/login";
  },
};

// Add global error handler for fetch requests
window.addEventListener("unhandledrejection", function (event) {
  if (event.reason && event.reason.status === 401) {
    AuthUtils.handleUnauthorized();
  }
});
