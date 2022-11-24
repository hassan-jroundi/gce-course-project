import { Ecurie } from './ecurie.model';
import { Immeuble } from './immeuble.model';
import { Site } from './site.model';
export class Chambre {
    id: number;
    nom: string;
    immeuble: Immeuble;
    nombreLits: number;
    isActif: boolean;
}