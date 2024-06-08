import React, { useState, useRef, useEffect } from 'react';

const ControlPanel = () => {
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const drawerRef = useRef(null);

  useEffect(() => {
    const closeDrawerOnClickOutside = (event) => {
      if (drawerRef.current && !drawerRef.current.contains(event.target)) {
        setIsDrawerOpen(false);
      }
    };

    document.addEventListener('mousedown', closeDrawerOnClickOutside);

    return () => {
      document.removeEventListener('mousedown', closeDrawerOnClickOutside);
    };
  }, []);

  const toggleDrawer = () => {
    setIsDrawerOpen(!isDrawerOpen);
  };

  return (
    <div className="flex h-full">
      {/* Drawer */}
      <aside ref={drawerRef} className={`bg-base-200 w-64 fixed inset-y-0 left-0 z-50 transition duration-300 ease-in-out transform ${isDrawerOpen ? 'translate-x-0' : '-translate-x-full'}`}>
        {/* Drawer Content */}
        <div className="flex flex-col h-full">
          <nav className="py-4 px-6">
            <ul className="space-y-4">
              <li className="transition-transform duration-300 ease-in-out transform hover:translate-x-1"><a href="#">Home</a></li>
              <li className="transition-transform duration-300 ease-in-out transform hover:translate-x-1"><a href="#">User Management</a></li>
              <li className="transition-transform duration-300 ease-in-out transform hover:translate-x-1"><a href="#">Job Management</a></li>
            </ul>
          </nav>
        </div>
      </aside>

      {/* Main Content */}
      <div className="flex flex-col flex-1">
        {/* Header */}
        <header className="bg-base-100 py-4 px-6">
          <div className="navbar">
            <div className="flex-none">
              <button className="btn btn-square btn-ghost" onClick={toggleDrawer}>
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-5 h-5 stroke-current"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M4 6h16M4 12h16M4 18h16"></path></svg>
              </button>
            </div>
            <div className="flex-1 flex justify-center">
              <a className="btn btn-ghost text-xl">Admin Panel</a>
            </div>
            <div className="flex-none">
              <button className="btn btn-square btn-ghost">
                <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-5 h-5 stroke-current"><path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 12h.01M12 12h.01M19 12h.01M6 12a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0zm7 0a1 1 0 11-2 0 1 1 0 012 0z"></path></svg>
              </button>
            </div>
          </div>
        </header>

        {/* Main Content */}
        <main className="flex-1 p-6 flex justify-center items-center mb-10 mt-20">
            {/* Your main content goes here */}
            <div className="stats shadow">
                <div className="stat">
                <div className="stat-figure text-secondary">
                    {/* User Icon */}
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 14l9-5-9-5-9 5 9 5z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 14l9-5-9-5-9 5 9 5z" />
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M3 12l9 5 9-5M3 12l9 5 9-5" />
                    </svg>
                </div>
                <div className="stat-title">Users</div>
                <div className="stat-value"></div>
                </div>
                <div className="stat">
                <div className="stat-figure text-secondary">
                    {/* Job Added Icon */}
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
                    </svg>
                </div>
                <div className="stat-title">Job Added</div>
                <div className="stat-value"></div>
                </div>
                <div className="stat">
                <div className="stat-figure text-secondary">
                    {/* Application Icon */}
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"></path>
                    </svg>
                </div>
                <div className="stat-title">Application</div>
                <div className="stat-value"></div>
                </div>
            </div>
        </main>
        {/* Footer */}
        <footer className="footer footer-center p-10 bg-base-200 text-base-content rounded mt-60">
            <nav className="grid grid-flow-col gap-4">
                <a className="link link-hover">About us</a>
                <a className="link link-hover">Contact</a>
            </nav> 
            <nav>
                <div className="grid grid-flow-col gap-4">
                <a><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" className="fill-current"><path d="M24 4.557c-.883.392-1.832.656-2.828.775 1.017-.609 1.798-1.574 2.165-2.724-.951.564-2.005.974-3.127 1.195-.897-.957-2.178-1.555-3.594-1.555-3.179 0-5.515 2.966-4.797 6.045-4.091-.205-7.719-2.165-10.148-5.144-1.29 2.213-.669 5.108 1.523 6.574-.806-.026-1.566-.247-2.229-.616-.054 2.281 1.581 4.415 3.949 4.89-.693.188-1.452.232-2.224.084.626 1.956 2.444 3.379 4.6 3.419-2.07 1.623-4.678 2.348-7.29 2.04 2.179 1.397 4.768 2.212 7.548 2.212 9.142 0 14.307-7.721 13.995-14.646.962-.695 1.797-1.562 2.457-2.549z"></path></svg></a>
                <a><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" className="fill-current"><path d="M19.615 3.184c-3.604-.246-11.631-.245-15.23 0-3.897.266-4.356 2.62-4.385 8.816.029 6.185.484 8.549 4.385 8.816 3.6.245 11.626.246 15.23 0 3.897-.266 4.356-2.62 4.385-8.816-.029-6.185-.484-8.549-4.385-8.816zm-10.615 12.816v-8l8 3.993-8 4.007z"></path></svg></a>
                <a><svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" className="fill-current"><path d="M9 8h-3v4h3v12h5v-12h3.642l.358-4h-4v-1.667c0-.955.192-1.333 1.115-1.333h2.885v-5h-3.808c-3.596 0-5.192 1.583-5.192 4.615v3.385z"></path></svg></a>
                </div>
            </nav> 
            <aside>
                <p>Copyright © 2024 - All right reserved by Wassim</p>
            </aside>
        </footer>
      </div>
    </div>
  );
};

export default ControlPanel;
