/** @type {import('tailwindcss').Config} */
module.exports = {
    content: [
      "./src/**/*.{js,jsx,ts,tsx}",
      'node_modules/flowbite-react/lib/esm/**/*.js'
    ],
    safelist: [
      'w-64',
      'w-1/2',
      'rounded-l-lg',
      'rounded-r-lg',
      'bg-gray-200',
      'grid-cols-4',
      'grid-cols-7',
      'h-6',
      'leading-6',
      'h-9',
      'leading-9',
      'shadow-lg'
    ],
    darkMode: 'class',
    theme: {
      extend: {},
    },
    plugins: [
      require('daisyui'),
      require('flowbite/plugin')
    ],
  }
  