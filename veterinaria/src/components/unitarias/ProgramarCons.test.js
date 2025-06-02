import React from 'react';
import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { ProgramarCons } from '../../pages/ProgramarCons';
import * as ClienteService from '../../services/ClienteService';
import * as MascotaService from '../../services/MascotaService';
import { MemoryRouter } from 'react-router-dom';

// Mock de servicios
jest.mock('../../services/ClienteService');
jest.mock('../../services/MascotaService');

describe('ProgramarCons Component', () => {
    beforeEach(() => {
        jest.clearAllMocks();
    });

    test('debería buscar cliente correctamente y avanzar a etapa de selección de mascota', async () => {
        const fakeCliente = { id: 1, nombre: 'Juan', apellido: 'Pérez' };
        const fakeMascotas = [{ id: 10, nombre: 'Firulais', especie: 'Perro', raza: 'Labrador', sexo: 'Macho', edad: 4 }];

        ClienteService.getClienteById.mockResolvedValue(fakeCliente);
        MascotaService.getMascotasByClienteId.mockResolvedValue(fakeMascotas);

        render(
          <MemoryRouter>
            <ProgramarCons />
          </MemoryRouter>
        );

        const input = screen.getByPlaceholderText('Buscar cliente por ID');
        const buscarBtn = screen.getByText('Buscar');

        fireEvent.change(input, { target: { value: '1' } });
        fireEvent.click(buscarBtn);

        // Espera a que se cargue la mascota y cambie la etapa
        await waitFor(() => {
            expect(ClienteService.getClienteById).toHaveBeenCalledWith('1');
            expect(MascotaService.getMascotasByClienteId).toHaveBeenCalledWith(1);
            expect(screen.getByText(/Seleccione una Mascota/i)).toBeInTheDocument();
            expect(screen.getByText('Firulais')).toBeInTheDocument();
        });
    });

    test('debería mostrar mensaje de error si no se encuentra el cliente', async () => {
        ClienteService.getClienteById.mockResolvedValue(null);

        render(
          <MemoryRouter>
            <ProgramarCons />
          </MemoryRouter>
        );

        fireEvent.change(screen.getByPlaceholderText('Buscar cliente por ID'), { target: { value: '99' } });
        fireEvent.click(screen.getByText('Buscar'));

        await waitFor(() => {
            expect(screen.getByText(/No se encontró ningún cliente/i)).toBeInTheDocument();
        });
    });
});

export default ProgramarCons;
