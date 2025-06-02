import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { MemoryRouter } from 'react-router-dom';
import RegisterCliForm from '../RegisterCliForm';
import * as ClienteService from '../../../services/ClienteService';

jest.mock('../../../services/ClienteService');

describe('🧵 RegisterCliForm', () => {
  beforeEach(() => {
    // Limpia los mocks antes de cada prueba
    ClienteService.createCliente.mockClear();
  });

  it('🧵 debería renderizar correctamente el formulario y permitir la navegación entre pasos', () => {
    render(
      <MemoryRouter>
        <RegisterCliForm />
      </MemoryRouter>
    );

    // Paso 1: Verifica que el título y campos básicos estén presentes
    expect(screen.getByText('Registro de Cliente')).toBeInTheDocument();
    expect(screen.getByLabelText('N. Identificación')).toBeInTheDocument();
    expect(screen.getByLabelText('Tipo de Identificación')).toBeInTheDocument();
    expect(screen.getByLabelText('Nombre')).toBeInTheDocument();
    expect(screen.getByLabelText('Apellido')).toBeInTheDocument();

    // Paso 1: Avanzar a paso 2
    const siguienteBtn = screen.getByRole('button', { name: /Siguiente/i });
    fireEvent.click(siguienteBtn);

    // Paso 2: Verifica que el paso 2 esté presente
    expect(screen.getByText('Información de Contacto')).toBeInTheDocument();
    expect(screen.getByLabelText('Fecha de Nacimiento')).toBeInTheDocument();
    expect(screen.getByLabelText('Teléfono')).toBeInTheDocument();
    expect(screen.getByLabelText('Correo electrónico')).toBeInTheDocument();

    // Paso 2: Regresar a paso 1
    const anteriorBtn = screen.getByRole('button', { name: /Anterior/i });
    fireEvent.click(anteriorBtn);
    expect(screen.getByText('Información Personal')).toBeInTheDocument();
  });

  it('🧵 debería mostrar errores de validación si los datos son inválidos', async () => {
    render(
      <MemoryRouter>
        <RegisterCliForm />
      </MemoryRouter>
    );

    // Simula ingresar datos inválidos en paso 1
    fireEvent.change(screen.getByLabelText('N. Identificación'), { target: { value: 'abc' } });
    fireEvent.change(screen.getByLabelText('Nombre'), { target: { value: '123' } });
    fireEvent.change(screen.getByLabelText('Apellido'), { target: { value: '456' } });

    const siguienteBtn = screen.getByRole('button', { name: /Siguiente/i });
    fireEvent.click(siguienteBtn);

    // Errores deberían mostrarse
    expect(await screen.findByText('El ID debe contener solo números')).toBeInTheDocument();
    expect(await screen.findByText('El nombre debe contener solo letras')).toBeInTheDocument();
    expect(await screen.findByText('El apellido debe contener solo letras')).toBeInTheDocument();
  });

  it('🧵 debería enviar los datos correctamente cuando el formulario es válido', async () => {
    // Mock del servicio
    ClienteService.createCliente.mockResolvedValueOnce({ data: { message: 'ok' } });

    render(
      <MemoryRouter>
        <RegisterCliForm />
      </MemoryRouter>
    );

    // Paso 1: Rellenar los campos
    fireEvent.change(screen.getByLabelText('N. Identificación'), { target: { value: '123456' } });
    fireEvent.change(screen.getByLabelText('Tipo de Identificación'), { target: { value: 'Cedula de ciudadania' } });
    fireEvent.change(screen.getByLabelText('Nombre'), { target: { value: 'Juan' } });
    fireEvent.change(screen.getByLabelText('Apellido'), { target: { value: 'Pérez' } });

    // Avanzar a paso 2
    const siguienteBtn = screen.getByRole('button', { name: /Siguiente/i });
    fireEvent.click(siguienteBtn);

    // Paso 2: Rellenar campos de contacto
    fireEvent.change(screen.getByLabelText('Fecha de Nacimiento'), { target: { value: '1990-01-01' } });
    fireEvent.change(screen.getByLabelText('Teléfono'), { target: { value: '123456789' } });
    fireEvent.change(screen.getByLabelText('Correo electrónico'), { target: { value: 'correo@ejemplo.com' } });

    // Enviar el formulario
    const registrarBtn = screen.getByRole('button', { name: /Registrar Cliente/i });
    fireEvent.click(registrarBtn);

    // Espera a que se haya llamado el servicio
    await waitFor(() => {
      expect(ClienteService.createCliente).toHaveBeenCalledWith({
        id: '123456',
        tipo_id: 'Cedula de ciudadania',
        nombre: 'Juan',
        apellido: 'Pérez',
        sexo: '', // sexo no se llena en la prueba
        fechaNacimiento: '1990-01-01',
        telefono: '123456789',
        email: 'correo@ejemplo.com'
      });
    });
  });
});
