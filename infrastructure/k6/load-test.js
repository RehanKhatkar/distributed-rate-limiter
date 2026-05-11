import http from 'k6/http';
import { sleep } from 'k6';

const algorithm = __ENV.ALGORITHM || 'fixed_window';

export const options = {
    vus: 50,
    duration: '30s',
};

export default function () {

    const clientId = `user-${__VU}`;

    http.get(
        `http://nginx/rate-limit/${algorithm}`,
        {
            headers: {
                'X-Client-Id': clientId
            }
        }
    );

    sleep(0.1);
}
