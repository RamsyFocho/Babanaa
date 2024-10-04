/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    './src/**/*.{html,js,ts,jsx,tsx}', // Ensure this matches the structure of your project
     './pages/**/*.{html,js,ts,jsx,tsx}', // For Next.js apps
        './components/**/*.{html,js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {},
  },
  plugins: [],
};
