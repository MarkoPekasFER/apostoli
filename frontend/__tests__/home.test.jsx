import { render, screen, fireEvent, waitFor, act } from '@testing-library/react';
import Home from '../pages/index';
import { useRouter } from 'next/router';
import '@testing-library/jest-dom';

// Mock next/router
jest.mock('next/router', () => ({
  useRouter: jest.fn(),
}));

// Mock GoogleMap component (dynamically loaded)
jest.mock('@/components/maps/GoogleMap', () => () => <div>Google Map</div>);

// Mock localStorage for token
const localStorageMock = (function () {
  let store = {};
  return {
    getItem(key) {
      return store[key] || null;
    },
    setItem(key, value) {
      store[key] = value.toString();
    },
    clear() {
      store = {};
    },
  };
})();
global.localStorage = localStorageMock;

describe('Home Page', () => {
  let pushMock;

  beforeEach(() => {
    // Reset the mock before each test
    pushMock = jest.fn();
    useRouter.mockReturnValue({
      push: pushMock,
      query: {},
      pathname: '/',
    });
    localStorage.clear();
  });

  it('renders the main heading', () => {
    render(<Home />);
    const heading = screen.getByRole('heading', { name: /Nesreće HR/i });
    expect(heading).toBeInTheDocument();
  });

  it('renders the Google Map component', () => {
    render(<Home />);
    const googleMap = screen.getByText('Google Map');
    expect(googleMap).toBeInTheDocument();
  });

  it('displays the login button when there is no token', () => {
    render(<Home />);
    const loginButton = screen.getByRole('link', { name: /Ulogiraj se za prijavu nesreće/i });
    expect(loginButton).toBeInTheDocument();
  });

  it('displays the report button when there is a token', async () => {
    localStorage.setItem('token', 'dummyToken');
    render(<Home />);
    
    const reportButton = screen.getByRole('link', { name: /Prijavi nesreću/i });
    expect(reportButton).toBeInTheDocument();
  });

  it('renders the footer content', () => {
    render(<Home />);
    const footerText = screen.getByText(/Unlucky d.o.o./i);
    expect(footerText).toBeInTheDocument();
  });

  it('renders app instructions component', () => {
    render(<Home />);
    const appInstructions = screen.getByText(/Za sigurnu i informiranu Hrvatsku/i);
    expect(appInstructions).toBeInTheDocument();
  });

  it('handles token fetching from localStorage correctly', () => {
    localStorage.setItem('token', 'dummyToken');
    render(<Home />);
    
    // Ensure token state is correctly set
    expect(screen.getByRole('link', { name: /Prijavi nesreću/i })).toBeInTheDocument();
  });
});
