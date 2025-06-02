import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import RegisterUserForm from '../specificComponents/RegisterUserForm';

// Mock del servicio
jest.mock('../../services/UsuarioService', () => ({
  SubmitRegister: jest.fn(),
}));

// Mock del useNavigate
const mockNavigate = jest.fn();
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useNavigate: () => mockNavigate,
}));

describe('RegisterUserForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renderiza correctamente el paso 1', () => {
    render(
      <MemoryRouter>
        <RegisterUserForm />
      </MemoryRouter>
    );

    expect(screen.getByText(/Registro de Personal/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/N. Identificación/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Tipo de Identificación/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Nombre/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Apellido/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Sexo/i)).toBeInTheDocument();
  });

  it('muestra errores de validación en el paso 1 si hay campos inválidos', async () => {
    render(
      <MemoryRouter>
        <RegisterUserForm />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText(/N. Identificación/i), { target: { value: 'abc' } });
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: '123' } });
    fireEvent.change(screen.getByLabelText(/Apellido/i), { target: { value: '456' } });

    const siguienteBtn = screen.getByRole('button', { name: /Siguiente/i });
    fireEvent.click(siguienteBtn);

    expect(await screen.findByText(/El ID debe contener solo números/i)).toBeInTheDocument();
    expect(screen.getByText(/El nombre debe contener solo letras/i)).toBeInTheDocument();
    expect(screen.getByText(/El apellido debe contener solo letras/i)).toBeInTheDocument();
  });

  it('permite avanzar al paso 2 si el paso 1 es válido', () => {
    render(
      <MemoryRouter>
        <RegisterUserForm />
      </MemoryRouter>
    );

    fireEvent.change(screen.getByLabelText(/N. Identificación/i), { target: { value: '123456' } });
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: 'Juan' } });
    fireEvent.change(screen.getByLabelText(/Apellido/i), { target: { value: 'Pérez' } });

    const siguienteBtn = screen.getByRole('button', { name: /Siguiente/i });
    fireEvent.click(siguienteBtn);

    // Paso 2
    expect(screen.getByText(/Información de Contacto/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Fecha de Nacimiento/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Teléfono/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/Correo electrónico/i)).toBeInTheDocument();
  });

  it('valida el correo electrónico en el paso 2', async () => {
    render(
      <MemoryRouter>
        <RegisterUserForm />
      </MemoryRouter>
    );

    // Paso 1 válido
    fireEvent.change(screen.getByLabelText(/N. Identificación/i), { target: { value: '123456' } });
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: 'Juan' } });
    fireEvent.change(screen.getByLabelText(/Apellido/i), { target: { value: 'Pérez' } });
    fireEvent.click(screen.getByRole('button', { name: /Siguiente/i }));

    // Paso 2 con email inválido
    fireEvent.change(screen.getByLabelText(/Correo electrónico/i), { target: { value: 'correo-invalido' } });
    fireEvent.click(screen.getByRole('button', { name: /Siguiente/i }));

    expect(await screen.findByText(/Ingrese un correo electrónico válido/i)).toBeInTheDocument();
  });

  it('envía el formulario correctamente cuando todos los pasos son válidos', async () => {
    const { SubmitRegister } = require('../../services/UsuarioService');

    render(
      <MemoryRouter>
        <RegisterUserForm />
      </MemoryRouter>
    );

    // Paso 1
    fireEvent.change(screen.getByLabelText(/N. Identificación/i), { target: { value: '123456' } });
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: 'Juan' } });
    fireEvent.change(screen.getByLabelText(/Apellido/i), { target: { value: 'Pérez' } });
    fireEvent.click(screen.getByRole('button', { name: /Siguiente/i }));

    // Paso 2
    fireEvent.change(screen.getByLabelText(/Fecha de Nacimiento/i), { target: { value: '1990-01-01' } });
    fireEvent.change(screen.getByLabelText(/Teléfono/i), { target: { value: '3216549870' } });
    fireEvent.change(screen.getByLabelText(/Correo electrónico/i), { target: { value: 'juan@mail.com' } });
    fireEvent.click(screen.getByRole('button', { name: /Siguiente/i }));

    // Paso 3
    fireEvent.change(screen.getByLabelText(/Rol/i), { target: { value: '2' } });
    fireEvent.change(screen.getByLabelText(/Fecha de Contrato/i), { target: { value: '2024-05-20' } });

    const submitButton = screen.getByRole('button', { name: /Registrarse/i });
    fireEvent.click(submitButton);

    await waitFor(() => {
      expect(SubmitRegister).toHaveBeenCalledTimes(1);
      expect(SubmitRegister).toHaveBeenCalledWith(
        '123456',
        '', // tipoIdentificacion no fue cambiado
        'Juan',
        'Pérez',
        '',
        '1990-01-01',
        '3216549870',
        '2024-05-20',
        'juan@mail.com',
        2,
        mockNavigate
      );
    });
  });
});
