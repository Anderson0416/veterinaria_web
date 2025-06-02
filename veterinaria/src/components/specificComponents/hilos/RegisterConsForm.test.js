import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import { createConsulta } from '../../../services/ConsultaService';
import RegisterConsForm from '../RegisterConsForm';

// Mockear createConsulta para que no haga llamadas reales a la API
jest.mock('../../../services/ConsultaService', () => ({
  createConsulta: jest.fn(),
}));

// Mockear useNavigate para evitar navegación real
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

describe('RegisterConsForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('debería mostrar errores cuando se envía el formulario vacío', async () => {
    render(
      <MemoryRouter>
        <RegisterConsForm />
      </MemoryRouter>
    );

    const submitButton = screen.getByRole('button', { name: /registrar consulta/i });
    fireEvent.click(submitButton);

    expect(await screen.findByText('El nombre es requerido')).toBeInTheDocument();
    expect(await screen.findByText('La descripción es requerida')).toBeInTheDocument();
    expect(await screen.findByText('El precio es requerido')).toBeInTheDocument();
  });

  test('debería mostrar error si el precio es inválido', async () => {
    render(
      <MemoryRouter>
        <RegisterConsForm />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText(/nombre/i), { target: { value: 'Consulta' } });
    fireEvent.change(screen.getByLabelText(/descripción/i), { target: { value: 'Detalle' } });
    fireEvent.change(screen.getByLabelText(/precio/i), { target: { value: '-10' } });

    const submitButton = screen.getByRole('button', { name: /registrar consulta/i });
    fireEvent.click(submitButton);

    expect(await screen.findByText('El precio debe ser un número mayor a 0')).toBeInTheDocument();
  });

  test('debería enviar el formulario y redirigir al usuario', async () => {
    createConsulta.mockResolvedValueOnce({}); // Respuesta de éxito simulada

    render(
      <MemoryRouter>
        <RegisterConsForm />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText(/nombre/i), { target: { value: 'Consulta General' } });
    fireEvent.change(screen.getByLabelText(/descripción/i), { target: { value: 'Consulta detallada' } });
    fireEvent.change(screen.getByLabelText(/precio/i), { target: { value: '50' } });

    const submitButton = screen.getByRole('button', { name: /registrar consulta/i });
    fireEvent.click(submitButton);

    // Esperamos a que la función createConsulta haya sido llamada
    await waitFor(() => {
      expect(createConsulta).toHaveBeenCalledWith({
        nombre: 'Consulta General',
        descripcion: 'Consulta detallada',
        precio: 50,
      });
      expect(mockNavigate).toHaveBeenCalledWith(-1); // Verifica que navegue hacia atrás
    });
  });
});
