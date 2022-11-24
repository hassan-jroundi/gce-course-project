import { Chambre } from './chambre.model';
import { Ecurie } from './ecurie.model';
import { Site } from './site.model';
export class Lit {
    id: number;
    nom: string;
    ordre: string;
    chambre: Chambre;
    prix: string;
    isActif: boolean;
}