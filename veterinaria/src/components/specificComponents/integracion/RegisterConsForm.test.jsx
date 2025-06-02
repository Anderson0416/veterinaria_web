import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import RegisterConsForm from '../RegisterConsForm';

// Mock del servicio
jest.mock('../../../services/ConsultaService', () => ({
  createConsulta: jest.fn(),
}));
import { createConsulta } from '../../../services/ConsultaService';

// Mock de NavigationControls y useNavigate
jest.mock('../../common/NavigationControls', () => () => <div data-testid="nav-controls" />);
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

describe('RegisterConsForm - integración', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('envía el formulario con datos válidos', async () => {
    createConsulta.mockResolvedValueOnce({}); // Mock respuesta exitosa

    render(
      <MemoryRouter>
        <RegisterConsForm />
      </MemoryRouter>
    );

    // Llenar los campos
    fireEvent.change(screen.getByLabelText(/nombre de la consulta/i), {
      target: { value: 'Consulta General' },
    });
    fireEvent.change(screen.getByLabelText(/descripción/i), {
      target: { value: 'Descripción de prueba' },
    });
    fireEvent.change(screen.getByLabelText(/precio/i), {
      target: { value: '100.50' },
    });

    // Enviar formulario
    fireEvent.click(screen.getByRole('button', { name: /registrar consulta/i }));

    await waitFor(() => {
      expect(createConsulta).toHaveBeenCalledWith({
        nombre: 'Consulta General',
        descripcion: 'Descripción de prueba',
        precio: 100.5,
      });
      expect(mockNavigate).toHaveBeenCalledWith(-1);
    });
  });

  test('muestra errores si se intenta enviar con campos vacíos', async () => {
    render(
      <MemoryRouter>
        <RegisterConsForm />
      </MemoryRouter>
    );

    fireEvent.click(screen.getByRole('button', { name: /registrar consulta/i }));

    expect(await screen.findByText(/el nombre es requerido/i)).toBeInTheDocument();
    expect(screen.getByText(/la descripción es requerida/i)).toBeInTheDocument();
    expect(screen.getByText(/el precio es requerido/i)).toBeInTheDocument();

    expect(createConsulta).not.toHaveBeenCalled();
    expect(mockNavigate).not.toHaveBeenCalled();
  });
});
