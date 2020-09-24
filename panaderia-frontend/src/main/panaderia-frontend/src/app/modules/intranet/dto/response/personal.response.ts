import { PersonaResponse } from './persona.response';

export class PersonalResponse {
    id: number;
    persona: PersonaResponse;
    cargo: string;
    flgActivo: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;
}