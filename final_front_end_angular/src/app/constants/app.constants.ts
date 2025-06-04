export const API_URL = {
    DEV: 'http://localhost:8092/api',
    PROD: 'https://api.eyewebapp.com/api'
} as const;


export const NAVIGATION = {
    ACCESS: '/auth/access',
    LOGIN: '/auth/login',
    REGISTER: '/auth/register',
    VERIFICATION: 'auth/verification',
    FORGOTPASSWORD: 'auth/forgotpassword',
    NEWPASSWORD: 'auth/newpassword',
    BACKOFFICE: 'back-office',
    PROFILE: 'profile',
    SUCCESS: 'auth/success',

    patientBiometrics: (patientId: string | number) => `cataract/patients/${patientId}/biometrics`,
    patientCalculators: (patientId: string | number) => `cataract/patients/${patientId}/calculators`,
    patientPreop: (patientId: string | number) => `cataract/patients/${patientId}/preop`
} as const;

export const ROLE = {
    ADMIN: 'ADMIN',
    USER: 'USER'
} as const;
