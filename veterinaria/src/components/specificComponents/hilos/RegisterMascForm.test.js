// src/components/consulta/__tests__/RegisterMascForm.test.jsx
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import RegisterMascForm from '../RegisterMascForm';
import * as ClienteService from '../../../services/ClienteService';
import * as MascotaService from '../../../services/MascotaService';

// Mock de los servicios
jest.mock('../../../services/ClienteService', () => ({
  getAllClientes: jest.fn(),
}));
jest.mock('../../../services/MascotaService', () => ({
  createMascota: jest.fn(),
}));

// Helper para renderizar con router
const renderWithRouter = (ui) => {
  return render(<BrowserRouter>{ui}</BrowserRouter>);
};

describe('RegisterMascForm', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('renderiza correctamente el paso 1', () => {
    renderWithRouter(<RegisterMascForm />);
    expect(screen.getByText('Registro de Mascota')).toBeInTheDocument();
    expect(screen.getByLabelText('Nombre')).toBeInTheDocument();
    expect(screen.getByLabelText('Especie')).toBeInTheDocument();
    expect(screen.getByLabelText('Raza')).toBeInTheDocument();
  });

  test('avanza al paso 2 y regresa al paso 1', async () => {
    renderWithRouter(<RegisterMascForm />);
    const siguienteBtn = screen.getByText(/Siguiente/i);
    fireEvent.click(siguienteBtn);

    expect(screen.getByText('Información Adicional')).toBeInTheDocument();

    const anteriorBtn = screen.getByText(/Anterior/i);
    fireEvent.click(anteriorBtn);

    expect(screen.getByText('Información Básica')).toBeInTheDocument();
  });

  test('muestra error si nombre tiene caracteres inválidos', async () => {
    renderWithRouter(<RegisterMascForm />);
    fireEvent.change(screen.getByLabelText('Nombre'), {
      target: { value: 'Nombre123' },
    });

    // Avanzar al paso 2
    fireEvent.click(screen.getByText(/Siguiente/i));
    // Rellenar campos requeridos
    fireEvent.change(screen.getByLabelText(/Edad/), {
      target: { value: '5' },
    });
    fireEvent.change(screen.getByLabelText(/ID del Cliente/), {
      target: { value: '1' },
    });

    // Simular submit
    fireEvent.click(screen.getByText(/Registrar Mascota/i));

    expect(await screen.findByText(/El nombre debe contener solo letras/)).toBeInTheDocument();
  });

  test('muestra error si edad es inválida', async () => {
    renderWithRouter(<RegisterMascForm />);
    fireEvent.click(screen.getByText(/Siguiente/i));

    fireEvent.change(screen.getByLabelText(/Edad/), {
      target: { value: '-1' },
    });
    fireEvent.change(screen.getByLabelText(/ID del Cliente/), {
      target: { value: '1' },
    });

    fireEvent.click(screen.getByText(/Registrar Mascota/i));

    expect(await screen.findByText(/La edad debe ser un número mayor a 0/)).toBeInTheDocument();
  });

  test('filtra sugerencias de clientes', async () => {
    const mockClientes = [
      { id: 1, nombre: 'Juan' },
      { id: 2, nombre: 'Pedro' },
    ];
    ClienteService.getAllClientes.mockResolvedValueOnce(mockClientes);

    renderWithRouter(<RegisterMascForm />);

    await waitFor(() => expect(ClienteService.getAllClientes).toHaveBeenCalled());

    // Avanzar a paso 2
    fireEvent.click(screen.getByText(/Siguiente/i));

    const clienteInput = screen.getByLabelText(/ID del Cliente/);
    fireEvent.change(clienteInput, { target: { value: 'Juan' } });

    await waitFor(() => {
      expect(screen.getByText(/Juan/)).toBeInTheDocument();
    });

    // Hacer clic en sugerencia
    fireEvent.click(screen.getByText(/Juan/));
    expect(clienteInput.value).toBe('1');
  });

  test('envía formulario con datos válidos', async () => {
    ClienteService.getAllClientes.mockResolvedValueOnce([{ id: 1, nombre: 'Juan' }]);
    MascotaService.createMascota.mockResolvedValueOnce({ success: true });

    renderWithRouter(<RegisterMascForm />);

    // Paso 1
    fireEvent.change(screen.getByLabelText('Nombre'), { target: { value: 'Firulais' } });
    fireEvent.change(screen.getByLabelText('Especie'), { target: { value: 'Perro' } });
    fireEvent.change(screen.getByLabelText('Raza'), { target: { value: 'Labrador' } });
    fireEvent.click(screen.getByText(/Siguiente/i));

    // Paso 2
    fireEvent.change(screen.getByLabelText(/Sexo/), { target: { value: 'Macho' } });
    fireEvent.change(screen.getByLabelText(/Edad/), { target: { value: '3' } });
    fireEvent.change(screen.getByLabelText(/ID del Cliente/), { target: { value: '1' } });

    fireEvent.click(screen.getByText(/Registrar Mascota/i));

    await waitFor(() => {
      expect(MascotaService.createMascota).toHaveBeenCalledWith({
        nombre: 'Firulais',
        especie: 'Perro',
        raza: 'Labrador',
        sexo: 'Macho',
        edad: 3,
        clienteId: '1',
      });
    });
  });
});
