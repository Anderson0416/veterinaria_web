// __tests__/RegisterMascForm.test.js
import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import RegisterMascForm from '../RegisterMascForm';
import * as ClienteService from '../../../services/ClienteService';
import * as MascotaService from '../../../services/MascotaService';

jest.mock('../../../services/ClienteService');
jest.mock('../../../services/MascotaService');

describe('RegisterMascForm - Interactions', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('should complete the form steps and submit', async () => {
    // Mock de clientes
    ClienteService.getAllClientes.mockResolvedValue([
      { id: 1, nombre: 'Juan Perez' },
      { id: 2, nombre: 'Maria Gomez' }
    ]);

    // Mock de creación de mascota
    MascotaService.createMascota.mockResolvedValue({ success: true });

    render(
      <BrowserRouter>
        <RegisterMascForm />
      </BrowserRouter>
    );

    // Paso 1
    const nombreInput = screen.getByPlaceholderText('Ingrese el nombre de la mascota');
    const especieSelect = screen.getByLabelText('Especie');
    const razaInput = screen.getByPlaceholderText('Ingrese la raza de la mascota');
    const nextButton = screen.getByRole('button', { name: /siguiente/i });

    fireEvent.change(nombreInput, { target: { value: 'Firulais' } });
    fireEvent.change(especieSelect, { target: { value: 'Perro' } });
    fireEvent.change(razaInput, { target: { value: 'Labrador' } });

    fireEvent.click(nextButton);

    // Paso 2
    const sexoSelect = screen.getByLabelText('Sexo');
    const edadInput = screen.getByPlaceholderText('Ingrese la edad en años');
    const clienteInput = screen.getByPlaceholderText('Ingrese el ID o nombre del propietario');

    fireEvent.change(sexoSelect, { target: { value: 'Macho' } });
    fireEvent.change(edadInput, { target: { value: '3' } });

    // Simula que el usuario escribe un nombre para filtrar clientes
    fireEvent.change(clienteInput, { target: { value: 'Juan' } });

    // Espera a que aparezcan las sugerencias
    await waitFor(() => {
      expect(screen.getByText('ID: 1')).toBeInTheDocument();
    });

    // Selecciona el cliente sugerido
    fireEvent.click(screen.getByText('ID: 1'));

    // Envía el formulario
    const submitButton = screen.getByRole('button', { name: /registrar mascota/i });
    fireEvent.click(submitButton);

    // Espera a que la mascota se haya registrado
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
