import {UserMessage} from "@/utils/UserMessage.tsx";
import {v4 as uuidv4} from 'uuid';

class WebSocketService {
    private userId: string = uuidv4();
    private ws: WebSocket | null = null;
    private static instance: WebSocketService;

    private constructor() {
        this.connect();
    }

    static getInstance() {
        if (!WebSocketService.instance) {
            WebSocketService.instance = new WebSocketService();
        }
        return WebSocketService.instance;
    }

    private connect() {
        const currentLocation = window.location;

        // Connect to the WebSocket server
        if (currentLocation.protocol === 'https:') {
            this.ws = new WebSocket('wss://' + location.host + '/ws');
        } else {
            this.ws = new WebSocket('ws://' + location.host + '/ws');
        }

        this.ws.onopen = () => {
            console.log('Connected to WebSocket');
        };

        this.ws.onerror = (error) => {
            console.error('WebSocket error:', error);
        };

        this.ws.onclose = () => {
            console.log('Disconnected from WebSocket');
            // Attempt to reconnect after 5 seconds
            setTimeout(() => this.connect(), 5000);
        };
    }

    sendMessage(message: string): Promise<string> {
        return new Promise((resolve, reject) => {
            if (!this.ws || this.ws.readyState !== WebSocket.OPEN) {
                reject(new Error('WebSocket is not connected'));
                return;
            }

            const messageId = Date.now().toString();

            const handleMessage = (event: MessageEvent) => {
                const response: UserMessage = JSON.parse(event.data);
                if (response.messageId === messageId) {
                    this.ws?.removeEventListener('message', handleMessage);
                    resolve(response.message);
                }
            };

            this.ws.addEventListener('message', handleMessage);

            const userMessage: UserMessage = {
                userId: this.userId,
                messageId: messageId,
                message: message
            };

            this.ws.send(JSON.stringify(userMessage));
        });
    }
}

export const wsService = WebSocketService.getInstance();