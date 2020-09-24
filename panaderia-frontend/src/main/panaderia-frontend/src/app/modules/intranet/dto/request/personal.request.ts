import { PersonaRequest } from './persona.request';

export class PersonalRequest {
    id: number;
    persona: PersonaRequest;
    cargo: string;
    flgActivo: number;
    idUsuarioCrea: number;
    fecUsuarioCrea: Date;
    idUsuarioMod: number;
    fecUsuarioMod: Date;

    constructor() {
        this.persona = new PersonaRequest();
    }
}