import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import RegisterUserForm from '../RegisterUserForm';
import * as UsuarioService from '../../../services/UsuarioService';

jest.mock('../../../services/UsuarioService');

describe('RegisterUserForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  const renderComponent = () => {
    return render(
      <BrowserRouter>
        <RegisterUserForm />
      </BrowserRouter>
    );
  };

  test('renders initial step correctly', () => {
    renderComponent();

    expect(screen.getByText(/Información Personal/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/N. Identificación/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Nombre/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Apellido/i)).toBeInTheDocument();
  });

  test('navigates to step 2 and back to step 1', () => {
    renderComponent();

    const nextButton = screen.getByText(/Siguiente/i);
    fireEvent.click(nextButton);

    expect(screen.getByText(/Información de Contacto/i)).toBeInTheDocument();

    const prevButton = screen.getByText(/Anterior/i);
    fireEvent.click(prevButton);

    expect(screen.getByText(/Información Personal/i)).toBeInTheDocument();
  });

  test('shows validation errors on invalid input', async () => {
    renderComponent();

    const nextButton = screen.getByText(/Siguiente/i);
    fireEvent.click(nextButton);

    // Avanza a paso 2 para email
    const emailInput = screen.getByLabelText(/Correo electrónico/i);
    fireEvent.change(emailInput, { target: { value: 'correoInvalido' } });

    // Vuelve atrás para probar validación de nombre
    const prevButton = screen.getByText(/Anterior/i);
    fireEvent.click(prevButton);

    const nombreInput = screen.getByLabelText(/Nombre/i);
    fireEvent.change(nombreInput, { target: { value: '123' } });

    // Envía formulario final
    const finalButton = screen.getByText(/Registrarse/i);
    fireEvent.click(finalButton);

    await waitFor(() => {
      expect(screen.getByText(/El nombre debe contener solo letras/i)).toBeInTheDocument();
      expect(screen.getByText(/Ingrese un correo electrónico válido/i)).toBeInTheDocument();
    });
  });

  test('submits the form with valid data', async () => {
    UsuarioService.SubmitRegister.mockResolvedValueOnce();

    renderComponent();

    // Paso 1
    fireEvent.change(screen.getByLabelText(/N. Identificación/i), { target: { value: '123' } });
    fireEvent.change(screen.getByLabelText(/Tipo de Identificación/i), { target: { value: 'Cedula de ciudadania' } });
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: 'Juan' } });
    fireEvent.change(screen.getByLabelText(/Apellido/i), { target: { value: 'Pérez' } });
    fireEvent.change(screen.getByLabelText(/Sexo/i), { target: { value: 'Masculino' } });

    fireEvent.click(screen.getByText(/Siguiente/i));

    // Paso 2
    fireEvent.change(screen.getByLabelText(/Fecha de Nacimiento/i), { target: { value: '2000-01-01' } });
    fireEvent.change(screen.getByLabelText(/Teléfono/i), { target: { value: '123456789' } });
    fireEvent.change(screen.getByLabelText(/Correo electrónico/i), { target: { value: 'correo@ejemplo.com' } });

    fireEvent.click(screen.getByText(/Siguiente/i));

    // Paso 3
    fireEvent.change(screen.getByLabelText(/Rol/i), { target: { value: '2' } });
    fireEvent.change(screen.getByLabelText(/Fecha de Contrato/i), { target: { value: '2024-01-01' } });

    // Envía formulario final
    const submitButton = screen.getByText(/Registrarse/i);
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(UsuarioService.SubmitRegister).toHaveBeenCalledWith(
        '123',
        'Cedula de ciudadania',
        'Juan',
        'Pérez',
        'Masculino',
        '2000-01-01',
        '123456789',
        '2024-01-01',
        'correo@ejemplo.com',
        2,
        expect.any(Function)
      );
    });
  });
});
