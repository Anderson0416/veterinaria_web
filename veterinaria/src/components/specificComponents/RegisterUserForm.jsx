import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { SubmitRegister } from '../../services/UsuarioService';
import '../../assets/styles/RegisterUserForm.css';
import logo from '../../assets/images/logo1.png';
import NavigationControls from '../common/NavigationControls';

export function RegisterUserForm() {
    const [formData, setFormData] = useState({
        id: '',
        tipoIdentificacion: '',
        nombre: '',
        apellido: '',
        sexo: '',
        fechaNacimiento: '',
        telefono: '',
        fechaContrato: '',
        login: '',
        rol: ''
    });
    const [errors, setErrors] = useState({});
    const [loginError, setLoginError] = useState('');
    const [currentStep, setCurrentStep] = useState(1);
    const navigate = useNavigate();

    const validateEmail = (email) => {
        const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return re.test(String(email).toLowerCase());
    };

    const validateStepFields = () => {
        const stepErrors = {};

        if (currentStep === 1) {
            if (!formData.id.trim()) stepErrors.id = 'El campo ID es obligatorio';
            else if (!/^\d+$/.test(formData.id)) stepErrors.id = 'El ID debe contener solo números';

            if (!formData.tipoIdentificacion.trim()) stepErrors.tipoIdentificacion = 'Seleccione un tipo de identificación';
            if (!formData.nombre.trim()) stepErrors.nombre = 'El nombre es obligatorio';
            else if (!/^[A-Za-zÁ-ÿ\s]+$/.test(formData.nombre)) stepErrors.nombre = 'El nombre debe contener solo letras';

            if (!formData.apellido.trim()) stepErrors.apellido = 'El apellido es obligatorio';
            else if (!/^[A-Za-zÁ-ÿ\s]+$/.test(formData.apellido)) stepErrors.apellido = 'El apellido debe contener solo letras';

            if (!formData.sexo.trim()) stepErrors.sexo = 'Seleccione un sexo';
        }

        if (currentStep === 2) {
            if (!formData.fechaNacimiento.trim()) stepErrors.fechaNacimiento = 'Seleccione la fecha de nacimiento';
            if (!formData.telefono.trim()) stepErrors.telefono = 'El teléfono es obligatorio';
            else if (!/^\d+$/.test(formData.telefono)) stepErrors.telefono = 'El teléfono debe contener solo números';

            if (!formData.login.trim()) stepErrors.login = 'El correo es obligatorio';
            else if (!validateEmail(formData.login)) stepErrors.login = 'Ingrese un correo electrónico válido';
        }

        if (currentStep === 3) {
            if (!formData.rol.trim()) stepErrors.rol = 'Seleccione un rol';
            if (!formData.fechaContrato.trim()) stepErrors.fechaContrato = 'Seleccione la fecha de contrato';
        }

        setErrors(stepErrors);

        if (Object.keys(stepErrors).length > 0) {
            const firstError = Object.values(stepErrors)[0];
            window.alert(firstError); // Mostrar primer error como ventana emergente
            return false;
        }

        return true;
    };

    const onChangeHandler = (event) => {
        const { name, value } = event.target;
        setFormData(prev => ({
            ...prev,
            [name]: value
        }));

        if (errors[name]) {
            setErrors(prev => ({
                ...prev,
                [name]: ''
            }));
        }

        if (loginError) {
            setLoginError('');
        }
    };

    const onSubmitRegister = async (e) => {
        e.preventDefault();
        if (validateStepFields()) {
            await SubmitRegister(
                formData.id,
                formData.tipoIdentificacion,
                formData.nombre,
                formData.apellido,
                formData.sexo,
                formData.fechaNacimiento,
                formData.telefono,
                formData.fechaContrato,
                formData.login,
                parseInt(formData.rol),
                navigate
            );
            console.log('Formulario enviado:', formData);
        }
    };

    const nextStep = () => {
        if (validateStepFields()) {
            setCurrentStep(currentStep + 1);
        }
    };

    const prevStep = () => {
        setCurrentStep(currentStep - 1);
    };

    const renderStep = () => {
        switch (currentStep) {
            case 1:
                return (
                    <div className="step-container">
                        <h3 className="step-title">Información Personal</h3>
                        {/* ID */}
                        <div className="form-group">
                            <label htmlFor="id">N. Identificación</label>
                            <input
                                type="text"
                                id="id"
                                name="id"
                                className={`form-control ${errors.id ? 'is-invalid' : ''}`}
                                value={formData.id}
                                onChange={onChangeHandler}
                                placeholder="Ingrese su número de identificación"
                            />
                        </div>

                        {/* Tipo de ID */}
                        <div className="form-group">
                            <label htmlFor="tipoIdentificacion">Tipo de Identificación</label>
                            <select
                                id="tipoIdentificacion"
                                name="tipoIdentificacion"
                                className="form-control"
                                value={formData.tipoIdentificacion}
                                onChange={onChangeHandler}
                            >
                                <option value="">Seleccione una opción</option>
                                <option value="Cedula de ciudadania">Cédula de ciudadanía</option>
                                <option value="Cedula de extranjeria">Cédula de extranjería</option>
                                <option value="Pasaporte">Pasaporte</option>
                            </select>
                        </div>

                        {/* Nombre */}
                        <div className="form-group">
                            <label htmlFor="nombre">Nombre</label>
                            <input
                                type="text"
                                id="nombre"
                                name="nombre"
                                className={`form-control ${errors.nombre ? 'is-invalid' : ''}`}
                                value={formData.nombre}
                                onChange={onChangeHandler}
                                placeholder="Ingrese su nombre"
                            />
                        </div>

                        {/* Apellido */}
                        <div className="form-group">
                            <label htmlFor="apellido">Apellido</label>
                            <input
                                type="text"
                                id="apellido"
                                name="apellido"
                                className={`form-control ${errors.apellido ? 'is-invalid' : ''}`}
                                value={formData.apellido}
                                onChange={onChangeHandler}
                                placeholder="Ingrese su apellido"
                            />
                        </div>

                        {/* Sexo */}
                        <div className="form-group">
                            <label htmlFor="sexo">Sexo</label>
                            <select
                                id="sexo"
                                name="sexo"
                                className="form-control"
                                value={formData.sexo}
                                onChange={onChangeHandler}
                            >
                                <option value="">Seleccione una opción</option>
                                <option value="Masculino">Masculino</option>
                                <option value="Femenino">Femenino</option>
                                <option value="Otro">Otro</option>
                            </select>
                        </div>

                        <div className="form-footer">
                            <button type="button" className="btn btn-next" onClick={nextStep}>
                                Siguiente →
                            </button>
                        </div>
                    </div>
                );

            case 2:
                return (
                    <div className="step-container">
                        <h3 className="step-title">Información de Contacto</h3>
                        {/* Fecha Nacimiento */}
                        <div className="form-group">
                            <label htmlFor="fechaNacimiento">Fecha de Nacimiento</label>
                            <input
                                type="date"
                                id="fechaNacimiento"
                                name="fechaNacimiento"
                                className="form-control"
                                value={formData.fechaNacimiento}
                                onChange={onChangeHandler}
                            />
                        </div>

                        {/* Teléfono */}
                        <div className="form-group">
                            <label htmlFor="telefono">Teléfono</label>
                            <input
                                type="text"
                                id="telefono"
                                name="telefono"
                                className={`form-control ${errors.telefono ? 'is-invalid' : ''}`}
                                value={formData.telefono}
                                onChange={onChangeHandler}
                                placeholder="Ingrese su teléfono"
                            />
                        </div>

                        {/* Email */}
                        <div className="form-group">
                            <label htmlFor="login">Correo electrónico</label>
                            <input
                                type="text"
                                id="login"
                                name="login"
                                className={`form-control ${errors.login ? 'is-invalid' : ''}`}
                                value={formData.login}
                                onChange={onChangeHandler}
                                placeholder="ejemplo@correo.com"
                            />
                        </div>

                        <div className="button-group">
                            <button type="button" className="btn btn-prev" onClick={prevStep}>← Anterior</button>
                            <button type="button" className="btn btn-next" onClick={nextStep}>Siguiente →</button>
                        </div>
                    </div>
                );

            case 3:
                return (
                    <div className="step-container">
                        <h3 className="step-title">Información Laboral</h3>
                        {/* Rol */}
                        <div className="form-group">
                            <label htmlFor="rol">Rol</label>
                            <select
                                id="rol"
                                name="rol"
                                className="form-control"
                                value={formData.rol}
                                onChange={onChangeHandler}
                            >
                                <option value="">Seleccione una opcion</option>
                                <option value="2">Recepcionista</option>
                                <option value="3">Veterinario</option>
                            </select>
                        </div>

                        {/* Fecha Contrato */}
                        <div className="form-group">
                            <label htmlFor="fechaContrato">Fecha de Contrato</label>
                            <input
                                type="date"
                                id="fechaContrato"
                                name="fechaContrato"
                                className="form-control"
                                value={formData.fechaContrato}
                                onChange={onChangeHandler}
                            />
                        </div>

                        <div className="button-group">
                            <button type="button" className="btn btn-prev" onClick={prevStep}>← Anterior</button>
                            <button type="submit" className="btn btn-submit">Registrarse ✔</button>
                        </div>
                    </div>
                );

            default:
                return null;
        }
    };

    return (
        <>
            <NavigationControls />
            <div className="register-page">
                <div className="register-container">
                    <div className="header">
                        <img src={logo} alt="guide-upc logo" className="logo" />
                        <h2>Registro de Personal</h2>
                    </div>

                    <div className="progress-container">
                        <div className="progress-bar">
                            <div className={`progress-step ${currentStep >= 1 ? 'active' : ''}`}>
                                <span className="step-number">1</span>
                                <span className="step-label">Personal</span>
                            </div>
                            <div className="progress-line"></div>
                            <div className={`progress-step ${currentStep >= 2 ? 'active' : ''}`}>
                                <span className="step-number">2</span>
                                <span className="step-label">Contacto</span>
                            </div>
                            <div className="progress-line"></div>
                            <div className={`progress-step ${currentStep >= 3 ? 'active' : ''}`}>
                                <span className="step-number">3</span>
                                <span className="step-label">Laboral</span>
                            </div>
                        </div>
                    </div>

                    <form onSubmit={onSubmitRegister}>
                        {renderStep()}
                    </form>
                </div>
            </div>
        </>
    );
}

export default RegisterUserForm;
