import http from 'k6/http';
import { sleep } from 'k6';

// run using
// docker run --rm --network jutjubic_default -v "./scripts:/scripts" grafana/k6 run /scripts/load-test.js
export const options = {
    vus: 200,
    duration: '120s',
};

export default function () {
    const url = 'http://nginx:80/api/videos/trending';

    const res = http.get(url);

    if (res.status !== 200) {
        console.log(`Error: Received status ${res.status}`);
    }

    sleep(0.1);
}