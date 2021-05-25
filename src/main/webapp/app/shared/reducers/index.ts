import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from 'app/modules/administration/user-management/user-management.reducer';
import register, { RegisterState } from 'app/modules/account/register/register.reducer';
import activate, { ActivateState } from 'app/modules/account/activate/activate.reducer';
import password, { PasswordState } from 'app/modules/account/password/password.reducer';
import settings, { SettingsState } from 'app/modules/account/settings/settings.reducer';
import passwordReset, { PasswordResetState } from 'app/modules/account/password-reset/password-reset.reducer';
// prettier-ignore
import client, {
  ClientState
} from 'app/entities/client/client.reducer';
// prettier-ignore
import chat, {
  ChatState
} from 'app/entities/chat/chat.reducer';
// prettier-ignore
import message, {
  MessageState
} from 'app/entities/message/message.reducer';
// prettier-ignore
import clientRating, {
  ClientRatingState
} from 'app/entities/client-rating/client-rating.reducer';
// prettier-ignore
import credential, {
  CredentialState
} from 'app/entities/credential/credential.reducer';
// prettier-ignore
import areaOfExpertise, {
  AreaOfExpertiseState
} from 'app/entities/area-of-expertise/area-of-expertise.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly register: RegisterState;
  readonly activate: ActivateState;
  readonly passwordReset: PasswordResetState;
  readonly password: PasswordState;
  readonly settings: SettingsState;
  readonly client: ClientState;
  readonly chat: ChatState;
  readonly message: MessageState;
  readonly clientRating: ClientRatingState;
  readonly credential: CredentialState;
  readonly areaOfExpertise: AreaOfExpertiseState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  applicationProfile,
  administration,
  userManagement,
  register,
  activate,
  passwordReset,
  password,
  settings,
  client,
  chat,
  message,
  clientRating,
  credential,
  areaOfExpertise,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
