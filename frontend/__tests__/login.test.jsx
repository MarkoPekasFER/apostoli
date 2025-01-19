import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import Login from '../pages/login'; // Adjust the path if necessary
import '@testing-library/jest-dom';

// Mock the router
jest.mock('next/compat/router', () => ({
  useRouter: jest.fn(() => ({ push: jest.fn() })),
}));

describe('Login Page', () => {
  beforeEach(() => {
    // Mock fetch API
    global.fetch = jest.fn(() =>
      Promise.resolve({
        ok: true,
        text: () => Promise.resolve('mocked-jwt-token'),
      })
    );
    jest.clearAllMocks();
  });

  afterAll(() => {
    delete global.fetch; // Clean up the global namespace
  });

  it('allows the user to submit the login form with random credentials', async () => {
    const { getByPlaceholderText, getByRole } = render(<Login />);

    // Simulate entering a username and password
    const usernameInput = getByPlaceholderText('Korisničko ime');
    const passwordInput = getByPlaceholderText('Šifra');
    const submitButton = screen.getAllByRole('button', { name: /Prijava/i }).find(
        (button) => button.type === 'submit'
      );
      
    fireEvent.change(usernameInput, { target: { value: 'randomUser' } });
    fireEvent.change(passwordInput, { target: { value: 'randomPassword' } });

    // Click the submit button
    fireEvent.click(submitButton);

    // Wait for the fetch API to resolve
    await waitFor(() => {
      expect(global.fetch).toHaveBeenCalledWith(
        `${process.env.NEXT_PUBLIC_API_URL}/api/v1/user/login`,
        expect.objectContaining({
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ username: 'randomUser', password: 'randomPassword' }),
        })
      );
    });

    // Verify token is stored and router is called
    await waitFor(() => {
      expect(localStorage.getItem('token')).toBe('mocked-jwt-token');
    });
  });
});
