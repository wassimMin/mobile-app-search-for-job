import React, { useState, useRef, useEffect } from 'react';
import { Bar } from 'react-chartjs-2';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

// modaledite
const EditUserModal = ({ user, isOpen, onClose, onSave }) => {
  const [name, setName] = useState(user?.name || '');
  const [email, setEmail] = useState(user?.email || '');
  const [password, setPassword] = useState('');

  useEffect(() => {
    if (user) {
      setName(user.name);
      setEmail(user.email);
    }
  }, [user]);

  const handleSave = () => {
    onSave({ ...user, name, email, password });
  };

  if (!isOpen) return null;

  return (
    <div id="crud-modal" tabIndex="-1" aria-hidden="true" className="fixed inset-0 z-50 flex items-center justify-center w-full h-full bg-black bg-opacity-75">
    <div className="relative w-full max-w-md p-4 h-full md:h-auto">
      <div className="relative bg-gray-800 rounded-lg shadow-lg dark:bg-gray-700">
        <div className="flex items-center justify-between p-4 md:p-5 border-b rounded-t dark:border-gray-600">
          <h3 className="text-lg font-semibold text-gray-100 dark:text-white">Edit User</h3>
          <button type="button" className="text-gray-400 bg-transparent hover:bg-gray-200 hover:text-gray-900 rounded-lg text-sm w-8 h-8 ms-auto inline-flex justify-center items-center dark:hover:bg-gray-600 dark:hover:text-white" onClick={onClose}>
            <svg className="w-3 h-3" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 14 14">
              <path stroke="currentColor" strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="m1 1 6 6m0 0 6 6M7 7l6-6M7 7l-6 6"/>
            </svg>
            <span className="sr-only">Close modal</span>
          </button>
        </div>
        <form className="p-4 md:p-5">
          <div className="mb-4">
            <label htmlFor="name" className="block mb-2 text-sm font-medium text-gray-100 dark:text-white">Name</label>
            <input type="text" id="name" value={name} onChange={(e) => setName(e.target.value)} className="bg-gray-700 border border-gray-300 text-gray-100 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" required />
          </div>
          <div className="mb-4">
            <label htmlFor="email" className="block mb-2 text-sm font-medium text-gray-100 dark:text-white">Email</label>
            <input type="email" id="email" value={email} onChange={(e) => setEmail(e.target.value)} className="bg-gray-700 border border-gray-300 text-gray-100 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" required />
          </div>
          <div className="mb-4">
            <label htmlFor="password" className="block mb-2 text-sm font-medium text-gray-100 dark:text-white">Password</label>
            <input type="password" id="password" value={password} onChange={(e) => setPassword(e.target.value)} className="bg-gray-700 border border-gray-300 text-gray-100 text-sm rounded-lg focus:ring-primary-600 focus:border-primary-600 block w-full p-2.5 dark:bg-gray-600 dark:border-gray-500 dark:placeholder-gray-400 dark:text-white dark:focus:ring-primary-500 dark:focus:border-primary-500" />
          </div>
          <button type="button" onClick={handleSave} className="text-white bg-blue-700 hover:bg-blue-800 focus:ring-4 focus:outline-none focus:ring-blue-300 font-medium rounded-lg text-sm px-5 py-2.5 text-center dark:bg-blue-600 dark:hover:bg-blue-700 dark:focus:ring-blue-800">Save</button>
        </form>
      </div>
    </div>
  </div>
  );
};




// control panel
const ControlPanel = () => {
  // navigte 
  const navigate = useNavigate();
  // 
  const [isDrawerOpen, setIsDrawerOpen] = useState(false);
  const [userCount,setUserCount] = useState(0);
  const [jobsCount,setjobsCount] = useState(0);
  const [applicationCount,setapplicationCount] = useState(0);
  const [userData, setUserData] = useState([]);
  const [jobsData, setJobsData] = useState([]);
  const [selectedUser, setSelectedUser] = useState(null);
  const [isEditModalOpen, setIsEditModalOpen] = useState(false);
  const [selectedSection, setSelectedSection] = useState('homePage');

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
  // fetch user count
  useEffect(()=>{
    const fetchUserCount = async()=>{
      try {
        const response = await axios.get('http://localhost:5000/api/users/count');
        setUserCount(response.data.userCount);
      } catch (error) {
        console.error('Error fetching user count:', error);
      }
    };
    fetchUserCount();
  },[]);
  // fetch jobs count
  useEffect(()=>{
    const fetchJobsCount = async()=>{
      try {
        const response = await axios.get('http://localhost:5000/api/jobs/count');
        setjobsCount(response.data.jobsCount);
      } catch (error) {
        console.error('Error fetching Jobs count:', error);
      }
    };
    fetchJobsCount();
  },[]);
  // fetch application count;
  useEffect(()=>{
    const fetchApplicationCount = async()=>{
      try {
        const response = await axios.get('http://localhost:5000/api/application/count');
        setapplicationCount(response.data.applicationCount);
      } catch (error) {
        console.error('Error fetching Application count:', error);
      }
    };
    fetchApplicationCount();
  },[]);
  // fetch user data
  useEffect(()=>{
    if(selectedSection === 'userManagement'){
      axios.get('http://localhost:5000/api/users').then(response =>{
        setUserData(response.data.users);
      })
      .catch(error=>{
        console.error('Error fetching user data:', error);
      });
    }
  },[selectedSection]);
  // fetch job data
  useEffect(()=>{
    if(selectedSection === 'jobManagement'){
      axios.get('http://localhost:5000/api/jobs').then(response =>{
      setJobsData(response.data.jobs);
      })
      .catch(error=>{
        console.error('Error fetching user data:', error);
      });
    }
  },[selectedSection]);
  const toggleDrawer = () => {
    setIsDrawerOpen(!isDrawerOpen);
  };
  // handle delete job button 
  const handleDeleteJob = async (jobId) => {
    try {
      await axios.delete(`http://localhost:5000/api/jobs/${jobId}`);
      
      axios.get('http://localhost:5000/api/jobs').then(response =>{
      setJobsData(response.data.jobs);
      })
      .catch(error=>{
        console.error('Error fetching user data:', error);
      });
    } catch (error) {
      console.error('Error deleting job:', error);
    }
  };
  // handle Delete user button
  const handleDeleteUser = async (userId) => {
    try {
        await axios.delete(`http://localhost:5000/api/users/${userId}`);
        const response = await axios.get('http://localhost:5000/api/users');
        setUserData(response.data.users);
    } catch (error) {
        console.error('Error deleting user:', error);
    }
};
// handle edited user 
const handleEditUser = (user) => {
  setSelectedUser(user);
  setIsEditModalOpen(true);
};
// function EDited User 
const handleSaveUser = async (updatedUser) => {
  try {
    await axios.put(`http://localhost:5000/api/users/${updatedUser.userid}`, updatedUser);
    const response = await axios.get('http://localhost:5000/api/users');
    setUserData(response.data.users);
    setIsEditModalOpen(false);
  } catch (error) {
    console.error('Error updating user:', error);
  }
};
// handle click add user 
const handleAddUserClick = ()=>{
  navigate('/AddUserPage');
}
  // SELECTION 
  const handleSectionClick = (section) => {
    setSelectedSection(section);
  };

  const renderTable = () => {
    switch (selectedSection) {
      case 'userManagement':
        return (
          <div>
            <button className="btn btn-ghost btn-xs text-green-500"
              onClick={handleAddUserClick}
            >Add User</button>
          <div className="overflow-x-auto">
            <table className="table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Delete Users</th>
                  <th>Edit Users</th>
                </tr>
              </thead>
              <tbody>
                {userData.map((user, index) => (
                  <tr key={index}>
                    <th>
                      <label>
                        <input type="checkbox" className="checkbox" />
                      </label>
                    </th>
                    <td>
                      <div className="flex items-center gap-3">
                        <div className="avatar">
                          <div className="mask mask-squircle w-12 h-12">
                            <img src="/porfile1.svg" alt="Profile Avatar" />
                            </div>
                          </div>
                         <div>
                          <div className="font-bold">{user.name}</div>
                        </div>
                      </div>
                    </td>
                    <td>{user.email}</td>
                    <th>
                      <button
                    className="btn btn-ghost btn-xs text-red-500"
                    onClick={()=>{handleDeleteUser(user.userid)}}
                    >Delete</button>
                    </th>
                    <th>
                      <button
                        className="btn btn-ghost btn-xs text-yellow-500"
                        onClick={() => { handleEditUser(user) }}>
                          Edit</button>

                    </th>
                  </tr>
                ))}
              </tbody>
              <tfoot>
                <tr>
                  <th></th>
                  <th>Name</th>
                  <th>Email</th>
                  <th>Delete Users</th>
                  <th>Edit Users</th>
                  
                </tr>
              </tfoot>
            </table>
          </div>
          </div>
        );
        break;
      case 'homePage':
        return(
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
                <div className="stat-value">{userCount}</div>
                </div>
                <div className="stat">
                <div className="stat-figure text-secondary">
                    {/* Job Added Icon */}
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M12 6V4m0 2a2 2 0 100 4m0-4a2 2 0 110 4m-6 8a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4m6 6v10m6-2a2 2 0 100-4m0 4a2 2 0 110-4m0 4v2m0-6V4"></path>
                    </svg>
                </div>
                <div className="stat-title">Job Added</div>
                <div className="stat-value">{jobsCount}</div>
                </div>
                <div className="stat">
                <div className="stat-figure text-secondary">
                    {/* Application Icon */}
                    <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" className="inline-block w-8 h-8 stroke-current">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth="2" d="M5 8h14M5 8a2 2 0 110-4h14a2 2 0 110 4M5 8v10a2 2 0 002 2h10a2 2 0 002-2V8m-9 4h4"></path>
                    </svg>
                </div>
                <div className="stat-title">Application</div>
                <div className="stat-value">{applicationCount}</div>
                </div>
            </div>
        );
        break;
      case 'jobManagement':
        return(
          <div>
      <div className="overflow-x-auto">
        <table className="table table-xs w-full">
          <thead className="bg-gray-800 text-white">
            <tr>
              <th className="px-4 py-2">#</th>
              <th className="px-4 py-2">Job Title</th>
              <th className="px-4 py-2">Company</th>
              <th className="px-4 py-2">Location</th>
              <th className="px-4 py-2">Created At</th>
              <th className="px-4 py-2">Actions</th> {/* Add column for actions */}
            </tr>
          </thead>
          <tbody>
            {jobsData.map((job, index) => (
              <tr key={index}>
                <td className="border px-4 py-2">{index + 1}</td>
                <td className="border px-4 py-2">{job.job_title}</td>
                <td className="border px-4 py-2">{job.company_name}</td>
                <td className="border px-4 py-2">{job.location}</td>
                <td className="border px-4 py-2">{job.created_at}</td>
                <td className="border px-4 py-2">
                  {/* Delete button */}
                  <button
                    className="btn btn-ghost btn-xs text-red-500"
                    onClick={() => handleDeleteJob(job.id)} 
                  >
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
          <tfoot className="bg-gray-800 text-white">
            <tr>
              <th className="px-4 py-2">#</th>
              <th className="px-4 py-2">Job Title</th>
              <th className="px-4 py-2">Company</th>
              <th className="px-4 py-2">Location</th>
              <th className="px-4 py-2">Created At</th>
              <th className="px-4 py-2">Actions</th> {/* Add column for actions */}
            </tr>
          </tfoot>
        </table>
      </div>
    </div>
        );
      default:
        return null;
    }
  };
  return (
    <div className="flex h-full">
      {/* Drawer */}
      <aside ref={drawerRef} className={`bg-base-200 w-64 fixed inset-y-0 left-0 z-50 transition duration-300 ease-in-out transform ${isDrawerOpen ? 'translate-x-0' : '-translate-x-full'}`}>
        {/* Drawer Content */}
        <div className="flex flex-col h-full">
          <nav className="py-4 px-6">
            <ul className="space-y-4">
              <li className="transition-transform duration-300 ease-in-out transform hover:translate-x-1">
              <button onClick={() => handleSectionClick('homePage')}>Home</button>
                </li>
              <li className="transition-transform duration-300 ease-in-out transform hover:translate-x-1">
                  <button onClick={() => handleSectionClick('userManagement')}>User Management</button>
                </li>
              <li className="transition-transform duration-300 ease-in-out transform hover:translate-x-1">
              <button onClick={() => handleSectionClick('jobManagement')}>Job Management</button>
                </li>
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
            {selectedSection === 'homePage' && (
                    <div>
                        {renderTable()}

                    </div>)}
            {selectedSection === 'userManagement' && (
              <div>
                {renderTable()}
                <EditUserModal
                user={selectedUser}
                isOpen={isEditModalOpen}
                onClose={() => setIsEditModalOpen(false)}
                onSave={handleSaveUser}/>
              </div>
            ) }
            {selectedSection === 'jobManagement' && (
              <div>
                {renderTable()}
              </div>
            )}

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

