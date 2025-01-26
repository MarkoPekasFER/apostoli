'use client';
import { useState, useEffect } from 'react';

interface Contact {
  id: number;
  name: string;
  number: string;
}

interface Instruction {
  id: number;
  title: string;
  description: string;
  disasterType: string;
  contact: Contact;
}

const DisasterInstructions = () => {
  const [instructions, setInstructions] = useState<Instruction[]>([]);
  const [selectedDisaster, setSelectedDisaster] = useState('EARTHQUAKE');
  const [showAll, setShowAll] = useState(false);
  const [newInstruction, setNewInstruction] = useState<Partial<Instruction>>({
    title: '',
    description: '',
    disasterType: 'EARTHQUAKE',
    contact: { id: 0, name: '', number: '' }
  });

  const disasterTypes = [
    'EARTHQUAKE', 'FLOOD', 'FIRE', 'HURRICANE',
    'TORNADO', 'TSUNAMI', 'VOLCANO', 'OTHER'
  ];

  const fetchInstructions = async (disasterType: string) => {
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_API_URL + `/api/v1/instruction/${disasterType}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      const data = await response.json();
      setInstructions(data);
    } catch (error) {
      console.error('Error fetching instructions:', error);
    }
  };

  const fetchAllInstructions = async () => {
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_API_URL + '/api/v1/instruction/all', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      const data = await response.json();
      setInstructions(data);
      setShowAll(true);
    } catch (error) {
      console.error('Error fetching all instructions:', error);
    }
  };

  const handleDelete = async (id: number) => {
    try {
      await fetch(process.env.NEXT_PUBLIC_API_URL + `/api/v1/instruction/delete/${id}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      setInstructions(prev => prev.filter(inst => inst.id !== id));
    } catch (error) {
      console.error('Error deleting instruction:', error);
    }
  };

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await fetch(process.env.NEXT_PUBLIC_API_URL + '/api/v1/instruction/create', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(newInstruction)
      });
      const createdInstruction = await response.json();
      setInstructions(prev => [...prev, createdInstruction]);
      setNewInstruction({
        title: '',
        description: '',
        disasterType: 'EARTHQUAKE',
        contact: { name: '', number: '' }
      });
    } catch (error) {
      console.error('Error creating instruction:', error);
    }
  };
  console.log(instructions)
  return (
    <div className="p-6 max-w-4xl mx-auto">
      <h1 className="text-2xl font-bold mb-6">Disaster Preparedness Instructions</h1>

      {/* Filter Section */}
      <div className="mb-8 flex gap-4 items-center">
        <select
          value={selectedDisaster}
          onChange={(e) => setSelectedDisaster(e.target.value)}
          className="p-2 border rounded"
        >
          {disasterTypes.map(type => (
            <option key={type} value={type}>{type}</option>
          ))}
        </select>
        <button
          onClick={() => fetchInstructions(selectedDisaster)}
          className="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600"
        >
          Filter Instructions
        </button>
        <button
          onClick={fetchAllInstructions}
          className="bg-gray-500 text-white px-4 py-2 rounded hover:bg-gray-600"
        >
          Show All
        </button>
      </div>

      {/* Instructions List */}
      <div className="mb-8">
        <h2 className="text-xl font-semibold mb-4">
          {showAll ? 'All Instructions' : `${selectedDisaster} Instructions`}
        </h2>
        <div className="space-y-4">
          {instructions.map(instruction => (
            <div key={instruction.id} className="p-4 border rounded shadow-sm">
              <h3 className="font-bold text-lg">{instruction.title}</h3>
              <p className="text-gray-600 mb-2">{instruction.description}</p>
              <div className="flex justify-between items-center">
                <div>
                  <span className="text-sm bg-gray-100 px-2 py-1 rounded">
                    {instruction.disasterType}
                  </span>
                  <p className="mt-2 text-sm">
                    Contact: {instruction?.contact?.name} - {instruction?.contact?.number}
                  </p>
                </div>
                <button
                  onClick={() => handleDelete(instruction.id)}
                  className="text-red-500 hover:text-red-700"
                >
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>
      </div>

      {/* Create Form */}
      <form onSubmit={handleCreate} className="border-t pt-6">
        <h2 className="text-xl font-semibold mb-4">Create New Instruction</h2>
        <div className="grid grid-cols-2 gap-4 mb-4">
          <input
            type="text"
            placeholder="Title"
            value={newInstruction.title}
            onChange={e => setNewInstruction(prev => ({...prev, title: e.target.value}))}
            className="p-2 border rounded"
            required
          />
          <select
            value={newInstruction.disasterType}
            onChange={e => setNewInstruction(prev => ({...prev, disasterType: e.target.value}))}
            className="p-2 border rounded"
          >
            {disasterTypes.map(type => (
              <option key={type} value={type}>{type}</option>
            ))}
          </select>
          <textarea
            placeholder="Description"
            value={newInstruction.description}
            onChange={e => setNewInstruction(prev => ({...prev, description: e.target.value}))}
            className="p-2 border rounded col-span-2"
            rows={3}
            required
          />
          <input
            type="text"
            placeholder="Contact Name"
            value={newInstruction.contact?.name}
            onChange={e => setNewInstruction(prev => ({
              ...prev,
              contact: { ...prev.contact!, name: e.target.value }
            }))}
            className="p-2 border rounded"
            required
          />
          <input
            type="tel"
            placeholder="Contact Number"
            value={newInstruction.contact?.number}
            onChange={e => setNewInstruction(prev => ({
              ...prev,
              contact: { ...prev.contact!, number: e.target.value }
            }))}
            className="p-2 border rounded"
            required
          />
        </div>
        <button
          type="submit"
          className="bg-green-500 text-white px-4 py-2 rounded hover:bg-green-600"
        >
          Create Instruction
        </button>
      </form>
    </div>
  );
};

export default DisasterInstructions;