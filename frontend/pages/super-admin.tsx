// pages/organizations/index.tsx
import { useEffect, useState } from 'react';

interface Organization {
  id: number;
  name: string;
  email: string;
  members: Array<{
    username: string;
    orgRank: string;
  }>;
}

interface PendingReport {
  id: number;
  title: string;
  description: string;
  status: string;
}

interface UserToken {
  sub: string;
  // Add other token fields as needed
}

export default function OrganizationManagement() {
  const [organizations, setOrganizations] = useState<Organization[]>([]);
  const [pendingReports, setPendingReports] = useState<PendingReport[]>([]);
  const [newOrg, setNewOrg] = useState({ name: '', email: '' });
  const [newMember, setNewMember] = useState('');
  const [currentUser, setCurrentUser] = useState<string | null>(null);
    console.log('currentUser:', pendingReports);
  useEffect(() => {
    const token = localStorage.getItem('token');
    if (token) {
      const decoded = token
      setCurrentUser(decoded);
      fetchData(token);
    }
  }, []);

  const fetchData = async (token: string) => {
    try {
      const orgsResponse = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/org/allOrganizations`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const orgs = await orgsResponse.json();
      setOrganizations(orgs);

      const reportsResponse = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/org/retrievePending`, {
        headers: { Authorization: `Bearer ${token}` },
      });
      const reports = await reportsResponse.json();
      setPendingReports(reports);
    } catch (error) {
      console.error('Error fetching data:', error);
    }
  };

  const createOrganization = async (e: React.FormEvent) => {
    e.preventDefault();
    const token = localStorage.getItem('token');
    try {
      const response = await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/org/create`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        },
        body: JSON.stringify(newOrg),
      });
      if (response.ok) {
        setNewOrg({ name: '', email: '' });
        fetchData(token!);
      }
    } catch (error) {
      console.error('Error creating organization:', error);
    }
  };

  const deleteOrganization = async (orgName: string) => {
    const token = localStorage.getItem('token');
    try {
      await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/org/delete/${orgName}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchData(token!);
    } catch (error) {
      console.error('Error deleting organization:', error);
    }
  };

  const handleMemberAction = async (action: string, orgName: string, username: string) => {
    const token = localStorage.getItem('token');
    try {
      await fetch(`${process.env.NEXT_PUBLIC_API_URL}/api/v1/org/${action}/${orgName}/${username}`, {
        method: 'POST',
        headers: { Authorization: `Bearer ${token}` },
      });
      fetchData(token!);
    } catch (error) {
      console.error(`Error performing ${action}:`, error);
    }
  };

  const isOwner = (org: Organization) => {
    return org?.members?.some(m => m.username === currentUser && m.orgRank === 'OWNER');
  };

  return (
    <div className="container mx-auto p-4">
      <h1 className="text-2xl font-bold mb-4">Organization Management</h1>

      {/* Create Organization Form */}
      <form onSubmit={createOrganization} className="mb-8 p-4 bg-gray-100 rounded">
        <h2 className="text-xl font-semibold mb-4">Create New Organization</h2>
        <div className="flex gap-4">
          <input
            type="text"
            placeholder="Organization Name"
            className="p-2 border rounded"
            value={newOrg.name}
            onChange={(e) => setNewOrg({ ...newOrg, name: e.target.value })}
          />
          <input
            type="email"
            placeholder="Email"
            className="p-2 border rounded"
            value={newOrg.email}
            onChange={(e) => setNewOrg({ ...newOrg, email: e.target.value })}
          />
          <button type="submit" className="bg-blue-500 text-white px-4 py-2 rounded">
            Create
          </button>
        </div>
      </form>

      {/* Organizations List */}
      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">Your Organizations</h2>
        {organizations.map((org) => (
          <div key={org.id} className="mb-4 p-4 border rounded">
            <div className="flex justify-between items-center">
              <div>
                <h3 className="text-lg font-semibold">{org.name}</h3>
                <p className="text-gray-600">{org.email}</p>
              </div>
              {isOwner(org) && (
                <button
                  onClick={() => deleteOrganization(org.name)}
                  className="bg-red-500 text-white px-3 py-1 rounded"
                >
                  Delete
                </button>
              )}
            </div>

            {/* Members List */}
            <div className="mt-4">
              <h4 className="font-semibold mb-2">Members</h4>
              {org?.members?.map((member) => (
                <div key={member.username} className="flex items-center gap-4 mb-2">
                  <span>{member.username}</span>
                  <span className="text-sm text-gray-500">({member.orgRank})</span>
                  {isOwner(org) && member.orgRank !== 'OWNER' && (
                    <div className="flex gap-2">
                      <button
                        onClick={() => handleMemberAction('promote', org.name, member.username)}
                        className="text-sm bg-green-500 text-white px-2 py-1 rounded"
                      >
                        Promote
                      </button>
                      <button
                        onClick={() => handleMemberAction('demote', org.name, member.username)}
                        className="text-sm bg-yellow-500 text-white px-2 py-1 rounded"
                      >
                        Demote
                      </button>
                      <button
                        onClick={() => handleMemberAction('removeMember', org.name, member.username)}
                        className="text-sm bg-red-500 text-white px-2 py-1 rounded"
                      >
                        Remove
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>

            {/* Add Member Form */}
            {isOwner(org) && (
              <div className="mt-4 flex gap-2">
                <input
                  type="text"
                  placeholder="Username to add"
                  className="p-2 border rounded flex-grow"
                  value={newMember}
                  onChange={(e) => setNewMember(e.target.value)}
                />
                <button
                  onClick={() => {
                    handleMemberAction('addMember', org.name, newMember);
                    setNewMember('');
                  }}
                  className="bg-blue-500 text-white px-4 py-2 rounded"
                >
                  Add Member
                </button>
              </div>
            )}
          </div>
        ))}
      </div>

      {/* Pending Reports */}
      <div>
        <h2 className="text-xl font-semibold mb-4">Pending Reports</h2>
        {/* {pendingReports?.map((report) => (
          <div key={report.id} className="mb-4 p-4 border rounded">
            <h3 className="font-semibold">{report.title}</h3>
            <p className="text-gray-600">{report.description}</p>
            <span className="text-sm text-yellow-600">Status: {report.status}</span>
          </div>
        ))} */}
      </div>
    </div>
  );
}