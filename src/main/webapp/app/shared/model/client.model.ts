import dayjs from 'dayjs';
import { ICredential } from 'app/shared/model/credential.model';
import { IClientRating } from 'app/shared/model/client-rating.model';
import { IAreaOfExpertise } from 'app/shared/model/area-of-expertise.model';
import { IChat } from 'app/shared/model/chat.model';

export interface IClient {
  id?: number;
  advocate?: boolean;
  biography?: string | null;
  birthdate?: string;
  firstname?: string;
  lastname?: string;
  oabnumber?: string | null;
  phone?: string;
  createdAt?: string | null;
  pictureContentType?: string | null;
  picture?: string | null;
  credential?: ICredential | null;
  clientRatings?: IClientRating[] | null;
  clientEvaluatorRatings?: IClientRating[] | null;
  areas?: IAreaOfExpertise[] | null;
  chatReceivers?: IChat[] | null;
  chatSenders?: IChat[] | null;
}

export const defaultValue: Readonly<IClient> = {
  advocate: false,
};
