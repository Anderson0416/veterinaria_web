import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ProgramarCons } from '../../../pages/ProgramarCons';
import * as ClienteService from '../../../services/ClienteService';
import * as MascotaService from '../../../services/MascotaService';
import * as ConsultaService from '../../../services/ConsultaService';
import * as CitaService from '../../../services/CitaService';
import * as UsuarioService from '../../../services/UsuarioService';
import { MemoryRouter } from 'react-router-dom';

jest.mock('../../../services/ClienteService');
jest.mock('../../../services/MascotaService');
jest.mock('../../../services/ConsultaService');
jest.mock('../../../services/CitaService');
jest.mock('../../../services/UsuarioService');

describe('Flujo completo de ProgramarCons (interacción)', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  test('debería permitir programar una cita completa', async () => {
    const fakeCliente = { id: 1, nombre: 'Juan', apellido: 'Pérez' };
    const fakeMascotas = [{ id: 10, nombre: 'Firulais', especie: 'Perro', raza: 'Labrador', sexo: 'Macho', edad: 4 }];
    const fakeVeterinarios = [{ id: 100, nombre: 'Ana', apellido: 'Gómez' }];
    const fakeConsultas = [
      { id: 200, nombre: 'Consulta General', descripcion: 'Chequeo', precio: 50 },
      { id: 201, nombre: 'Vacunación', descripcion: 'Vacuna anual', precio: 30 }
    ];

    ClienteService.getClienteById.mockResolvedValue(fakeCliente);
    MascotaService.getMascotasByClienteId.mockResolvedValue(fakeMascotas);
    UsuarioService.getveterinarians.mockResolvedValue(fakeVeterinarios);
    ConsultaService.getAllConsultas.mockResolvedValue(fakeConsultas);
    CitaService.createCita.mockResolvedValue({ mensaje: 'Cita creada' });

    render(
      <MemoryRouter>
        <ProgramarCons />
      </MemoryRouter>
    );

    // Buscar cliente
    fireEvent.change(screen.getByPlaceholderText('Buscar cliente por ID'), { target: { value: '1' } });
    fireEvent.click(screen.getByText('Buscar'));

    // Selección de mascota
    await waitFor(() => screen.getByText('Firulais'));
    fireEvent.click(screen.getByText('Firulais'));

    // Selección de veterinario
    await waitFor(() => screen.getByText('Ana Gómez'));
    fireEvent.click(screen.getByText('Ana Gómez'));

    // Selección de consultas
    await waitFor(() => {
      expect(screen.getByText('Consulta General')).toBeInTheDocument();
      expect(screen.getByText('Vacunación')).toBeInTheDocument();
    });
    fireEvent.click(screen.getByText('Consulta General'));
    fireEvent.click(screen.getByText('Vacunación'));
    fireEvent.click(screen.getByText(/Continuar a Detalles de la Cita/i));

    // Detalles de cita
    const dateInput = screen.getByLabelText(/Fecha y Hora de la Cita/i);
    fireEvent.change(dateInput, { target: { value: '2025-12-01T10:00' } });
    fireEvent.change(screen.getByPlaceholderText(/información adicional/i), { target: { value: 'Traer historial médico' } });

    // Confirmar cita
    fireEvent.click(screen.getByText('Confirmar Cita'));

    await waitFor(() => {
      expect(CitaService.createCita).toHaveBeenCalled();
      expect(screen.getByText(/Cita programada exitosamente!/i)).toBeInTheDocument();
    });
  });
})