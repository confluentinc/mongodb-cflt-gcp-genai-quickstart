class WebSocketService {
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
    this.ws = new WebSocket('wss://your-websocket-endpoint');

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
        const response = JSON.parse(event.data);
        if (response.messageId === messageId) {
          this.ws?.removeEventListener('message', handleMessage);
          resolve(response.text);
        }
      };

      this.ws.addEventListener('message', handleMessage);

      this.ws.send(JSON.stringify({
        messageId,
        text: message
      }));
    });
  }
}

export const wsService = WebSocketService.getInstance();