import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ProgramarCons } from '../../../pages/ProgramarCons';
import * as ClienteService from '../../../services/ClienteService';
import * as MascotaService from '../../../services/MascotaService';
import * as UsuarioService from '../../../services/UsuarioService';
import * as ConsultaService from '../../../services/ConsultaService';
import * as CitaService from '../../../services/CitaService';
import { MemoryRouter } from 'react-router-dom';

// Mock de servicios
jest.mock('../../../services/ClienteService');
jest.mock('../../../services/MascotaService');
jest.mock('../../../services/UsuarioService');
jest.mock('../../../services/ConsultaService');
jest.mock('../../../services/CitaService');

describe('Flujo completo en ProgramarCons (basado en hilos)', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('flujo completo desde búsqueda hasta programación exitosa', async () => {
        // Mocks de datos
        const fakeCliente = { id: 1, nombre: 'Juan', apellido: 'Pérez' };
        const fakeMascotas = [{ id: 10, nombre: 'Firulais', especie: 'Perro', raza: 'Labrador', sexo: 'Macho', edad: 4 }];
        const fakeVeterinarios = [{ id: 100, nombre: 'Dra. Ana', apellido: 'Sánchez' }];
        const fakeConsultas = [
            { id: 200, nombre: 'Consulta General', descripcion: 'Chequeo', precio: 30.0 },
            { id: 201, nombre: 'Vacuna', descripcion: 'Vacuna anual', precio: 50.0 }
        ];
        const fakeCitaResponse = { id: 1, status: 'success' };

        // Mockear servicios
        ClienteService.getClienteById.mockResolvedValue(fakeCliente);
        MascotaService.getMascotasByClienteId.mockResolvedValue(fakeMascotas);
        UsuarioService.getveterinarians.mockResolvedValue(fakeVeterinarios);
        ConsultaService.getAllConsultas.mockResolvedValue(fakeConsultas);
        CitaService.createCitaWithFactura.mockResolvedValue(fakeCitaResponse);

        render(
            <MemoryRouter>
                <ProgramarCons />
            </MemoryRouter>
        );

        // Paso 1: Buscar cliente
        fireEvent.change(screen.getByPlaceholderText('Buscar cliente por ID'), {
            target: { value: '1' }
        });
        fireEvent.click(screen.getByText('Buscar'));

        await waitFor(() => {
            expect(screen.getByText('Seleccione una Mascota')).toBeInTheDocument();
            expect(screen.getByText('Firulais')).toBeInTheDocument();
        });

        // Paso 2: Seleccionar mascota
        fireEvent.click(screen.getByText('Firulais'));

        await waitFor(() => {
            expect(screen.getByText('Seleccione un Veterinario')).toBeInTheDocument();
            expect(screen.getByText(/Ana Sánchez/)).toBeInTheDocument();
        });

        // Paso 3: Seleccionar veterinario
        fireEvent.click(screen.getByText(/Ana Sánchez/));

        await waitFor(() => {
            expect(screen.getByText('Seleccione las Consultas')).toBeInTheDocument();
        });

        // Paso 4: Seleccionar consultas
        fireEvent.click(screen.getByText('Consulta General'));
        fireEvent.click(screen.getByText('Vacuna'));
        fireEvent.click(screen.getByText(/Continuar a Detalles de la Cita/));

        await waitFor(() => {
            expect(screen.getByText('Detalles de la Cita')).toBeInTheDocument();
        });

        // Paso 5: Seleccionar fecha y confirmar cita
        const dateInput = screen.getByLabelText(/Fecha y Hora de la Cita/i);
        const validDate = new Date(Date.now() + 60 * 60 * 1000).toISOString().slice(0, 16); // 1 hora en el futuro
        fireEvent.change(dateInput, { target: { value: validDate } });

        fireEvent.click(screen.getByText('Confirmar Cita'));

        await waitFor(() => {
            expect(screen.getByText(/Cita programada exitosamente/i)).toBeInTheDocument();
            expect(CitaService.createCitaWithFactura).toHaveBeenCalled();
        });
    });
});
