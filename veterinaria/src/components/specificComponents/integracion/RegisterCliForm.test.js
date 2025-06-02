import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import RegisterCliForm from '../RegisterCliForm';
import { createCliente } from '../../../services/ClienteService';
import { BrowserRouter } from 'react-router-dom';

// Mock del servicio
jest.mock('../../../services/ClienteService', () => ({
  createCliente: jest.fn(),
}));

// Mock para evitar errores de navegación
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate
}));

const renderWithRouter = (ui) => {
  return render(<BrowserRouter>{ui}</BrowserRouter>);
};

describe('RegisterCliForm - integración', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('flujo completo de registro exitoso', async () => {
    createCliente.mockResolvedValue({}); // Simula respuesta exitosa

    renderWithRouter(<RegisterCliForm />);

    // Paso 1: Información Personal
    userEvent.type(screen.getByLabelText(/N\. Identificación/i), '123456');
    userEvent.selectOptions(screen.getByLabelText(/Tipo de Identificación/i), 'Cedula de ciudadania');
    userEvent.type(screen.getByLabelText(/Nombre/i), 'Juan');
    userEvent.type(screen.getByLabelText(/Apellido/i), 'Pérez');
    userEvent.selectOptions(screen.getByLabelText(/Sexo/i), 'Masculino');

    // Click en siguiente
    userEvent.click(screen.getByRole('button', { name: /Siguiente/i }));

    // Paso 2: Información de Contacto
    userEvent.type(screen.getByLabelText(/Fecha de Nacimiento/i), '1990-01-01');
    userEvent.type(screen.getByLabelText(/Teléfono/i), '3214567890');
    userEvent.type(screen.getByLabelText(/Correo electrónico/i), 'juan@example.com');

    // Click en registrar
    userEvent.click(screen.getByRole('button', { name: /Registrar Cliente/i }));

    // Verifica que createCliente fue llamado con los datos correctos
    await waitFor(() => {
      expect(createCliente).toHaveBeenCalledWith({
        id: '123456',
        tipo_id: 'Cedula de ciudadania',
        nombre: 'Juan',
        apellido: 'Pérez',
        sexo: 'Masculino',
        fechaNacimiento: '1990-01-01',
        telefono: '3214567890',
        email: 'juan@example.com'
      });
    });

    // Verifica que se hizo la navegación
    expect(mockNavigate).toHaveBeenCalledWith(-1);
  });
});
