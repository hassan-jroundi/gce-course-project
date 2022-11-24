import { Site } from './site.model';
export class Piste {
    id: number;
    nom: string;
    numero: string;
    site: Site;
    prix: string;
    isActif: boolean;
}