import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import RegisterMascForm from '../specificComponents/RegisterMascForm';
import * as ClienteService from '../../services/ClienteService';
import * as MascotaService from '../../services/MascotaService';

// Mocks de servicios
jest.mock('../../services/ClienteService');
jest.mock('../../services/MascotaService');

describe('RegisterMascForm', () => {
  beforeEach(() => {
    ClienteService.getAllClientes.mockResolvedValue([
      { id: 1, nombre: 'Juan Pérez' },
      { id: 2, nombre: 'Ana Torres' },
    ]);
  });

  test('muestra errores si el formulario es inválido', async () => {
    render(
      <BrowserRouter>
        <RegisterMascForm />
      </BrowserRouter>
    );

    // Paso 1: datos inválidos
    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: '123' } });
    fireEvent.click(screen.getByText(/Siguiente/i));

    // Paso 2: datos inválidos
    fireEvent.change(screen.getByLabelText(/Edad/i), { target: { value: '-2' } });
    fireEvent.click(screen.getByText(/Registrar Mascota/i));

    await waitFor(() => {
      expect(screen.getByText(/El nombre debe contener solo letras/i)).toBeInTheDocument();
      expect(screen.getByText(/La edad debe ser un número mayor a 0/i)).toBeInTheDocument();
      expect(screen.getByText(/El ID del cliente es requerido/i)).toBeInTheDocument();
    });
  });

  test('envía el formulario correctamente', async () => {
    MascotaService.createMascota.mockResolvedValue({});

    render(
      <BrowserRouter>
        <RegisterMascForm />
      </BrowserRouter>
    );

    fireEvent.change(screen.getByLabelText(/Nombre/i), { target: { value: 'Firulais' } });
    fireEvent.change(screen.getByLabelText(/Especie/i), { target: { value: 'Perro' } });
    fireEvent.change(screen.getByLabelText(/Raza/i), { target: { value: 'Labrador' } });

    fireEvent.click(screen.getByText(/Siguiente/i));

    fireEvent.change(screen.getByLabelText(/Sexo/i), { target: { value: 'Macho' } });
    fireEvent.change(screen.getByLabelText(/Edad/i), { target: { value: '3' } });
    fireEvent.change(screen.getByLabelText(/ID del Cliente/i), { target: { value: '1' } });

    await waitFor(() => {
      fireEvent.click(screen.getByText(/Registrar Mascota/i));
    });

    await waitFor(() => {
      expect(MascotaService.createMascota).toHaveBeenCalledWith({
        nombre: 'Firulais',
        especie: 'Perro',
        raza: 'Labrador',
        sexo: 'Macho',
        edad: 3,
        clienteId: '1'
      });
    });
  });
});
