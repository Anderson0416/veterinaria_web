import { render, screen, fireEvent, waitFor } from '@testing-library/react';
import { BrowserRouter } from 'react-router-dom';
import { RegisterUserForm } from '../RegisterUserForm';
import * as UsuarioService from '../../../services/UsuarioService';

jest.mock('../../../services/UsuarioService', () => ({
  SubmitRegister: jest.fn(),
}));

describe('RegisterUserForm', () => {
  const setup = () => {
    render(
      <BrowserRouter>
        <RegisterUserForm />
      </BrowserRouter>
    );
  };

  it('permite al usuario completar el formulario paso a paso y enviarlo', async () => {
    setup();

    // Paso 1 - Información Personal
    const idInput = screen.getByPlaceholderText('Ingrese su número de identificación');
    const nombreInput = screen.getByPlaceholderText('Ingrese su nombre');
    const apellidoInput = screen.getByPlaceholderText('Ingrese su apellido');
    const tipoIdentificacionSelect = screen.getByLabelText('Tipo de Identificación');
    const sexoSelect = screen.getByLabelText('Sexo');
    const siguienteBtn = screen.getByText(/siguiente/i);

    // Completa campos obligatorios del paso 1
    fireEvent.change(idInput, { target: { value: '123456' } });
    fireEvent.change(nombreInput, { target: { value: 'Juan' } });
    fireEvent.change(apellidoInput, { target: { value: 'Pérez' } });
    fireEvent.change(tipoIdentificacionSelect, { target: { value: 'Cedula de ciudadania' } });
    fireEvent.change(sexoSelect, { target: { value: 'Masculino' } });

    // Avanza al siguiente paso
    fireEvent.click(siguienteBtn);

    // Paso 2 - Información de Contacto
    const fechaNacimientoInput = screen.getByLabelText('Fecha de Nacimiento');
    const telefonoInput = screen.getByPlaceholderText('Ingrese su teléfono');
    const loginInput = screen.getByPlaceholderText('ejemplo@correo.com');

    fireEvent.change(fechaNacimientoInput, { target: { value: '1990-01-01' } });
    fireEvent.change(telefonoInput, { target: { value: '987654321' } });
    fireEvent.change(loginInput, { target: { value: 'correo@ejemplo.com' } });

    const siguienteBtn2 = screen.getByText(/siguiente/i);
    fireEvent.click(siguienteBtn2);

    // Paso 3 - Información Laboral
    const rolSelect = screen.getByLabelText('Rol');
    const fechaContratoInput = screen.getByLabelText('Fecha de Contrato');

    fireEvent.change(rolSelect, { target: { value: '2' } });
    fireEvent.change(fechaContratoInput, { target: { value: '2024-01-01' } });

    // Envía el formulario
    const registrarBtn = screen.getByText(/registrarse/i);
    fireEvent.click(registrarBtn);

    // Verifica que SubmitRegister se haya llamado con los datos esperados
    await waitFor(() => {
      expect(UsuarioService.SubmitRegister).toHaveBeenCalledWith(
        '123456', 
        'Cedula de ciudadania',
        'Juan', 
        'Pérez',
        'Masculino',
        '1990-01-01',
        '987654321',
        '2024-01-01',
        'correo@ejemplo.com',
        2, 
        expect.any(Function)
      );
    });
  });

  it('muestra errores cuando los campos son inválidos', () => {
    setup();

    // Paso 1 - dejar campos vacíos y hacer clic en Siguiente
    const siguienteBtn = screen.getByText(/siguiente/i);
    fireEvent.click(siguienteBtn);

    expect(screen.getByText(/El ID debe contener solo números/i)).toBeInTheDocument();
    expect(screen.getByText(/El nombre debe contener solo letras/i)).toBeInTheDocument();
    expect(screen.getByText(/El apellido debe contener solo letras/i)).toBeInTheDocument();

    // Avanzar manualmente a paso 2 y validar email inválido
    fireEvent.click(siguienteBtn); // Ir a paso 2

    const loginInput = screen.getByPlaceholderText('ejemplo@correo.com');
    fireEvent.change(loginInput, { target: { value: 'correo_invalido' } });

    const siguienteBtn2 = screen.getByText(/siguiente/i);
    fireEvent.click(siguienteBtn2);

    expect(screen.getByText(/Ingrese un correo electrónico válido/i)).toBeInTheDocument();
  });
});
